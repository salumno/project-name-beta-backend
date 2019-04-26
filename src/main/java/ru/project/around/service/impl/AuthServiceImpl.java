package ru.project.around.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.project.around.model.entity.BusinessCard;
import ru.project.around.model.entity.EntryControl;
import ru.project.around.model.entity.PhoneCheckCode;
import ru.project.around.model.entity.User;
import ru.project.around.model.marker.EntryProcessMarker;
import ru.project.around.model.marker.PhoneCheckProcessMarker;
import ru.project.around.model.params.*;
import ru.project.around.model.status.EntryControlStatusEnum;
import ru.project.around.model.status.PhoneCheckCodeStatus;
import ru.project.around.model.status.UserPremiumStatus;
import ru.project.around.model.status.UserStatus;
import ru.project.around.repository.BusinessCardRepository;
import ru.project.around.repository.EntryControlRepository;
import ru.project.around.repository.PhoneCheckCodeRepository;
import ru.project.around.repository.UserRepository;
import ru.project.around.service.AuthService;
import ru.project.around.util.SmsUtils;
import ru.project.around.util.factory.EntryControlFactory;
import ru.project.around.util.factory.EntryProcessMarkerFactory;
import ru.project.around.util.factory.PhoneCheckCodeFactory;
import ru.project.around.util.factory.PhoneCheckProcessMarkerFactory;
import ru.project.around.util.jwt.JwtTokenUtils;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BusinessCardRepository businessCardRepository;
    private final EntryControlRepository entryControlRepository;
    private final PhoneCheckCodeRepository phoneCheckCodeRepository;

    private final EntryControlFactory entryControlFactory;
    private final PhoneCheckCodeFactory phoneCheckCodeFactory;
    private final EntryProcessMarkerFactory entryProcessMarkerFactory;
    private final PhoneCheckProcessMarkerFactory phoneCheckProcessMarkerFactory;

    private final JwtTokenUtils tokenUtils;
    private final SmsUtils smsUtils;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(final UserRepository userRepository, final BusinessCardRepository businessCardRepository,
                           final EntryControlRepository entryControlRepository, final PhoneCheckCodeRepository phoneCheckCodeRepository,
                           final EntryControlFactory entryControlFactory, final PhoneCheckCodeFactory phoneCheckCodeFactory,
                           final EntryProcessMarkerFactory entryProcessMarkerFactory,
                           final PhoneCheckProcessMarkerFactory phoneCheckProcessMarkerFactory,
                           final JwtTokenUtils tokenUtils, final SmsUtils smsUtils, final BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.businessCardRepository = businessCardRepository;
        this.entryControlRepository = entryControlRepository;
        this.phoneCheckCodeRepository = phoneCheckCodeRepository;
        this.entryControlFactory = entryControlFactory;
        this.phoneCheckCodeFactory = phoneCheckCodeFactory;
        this.entryProcessMarkerFactory = entryProcessMarkerFactory;
        this.phoneCheckProcessMarkerFactory = phoneCheckProcessMarkerFactory;
        this.tokenUtils = tokenUtils;
        this.smsUtils = smsUtils;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthTokens refreshTokens(final RefreshTokensParams refreshTokensParams) {
        final Long userId = tokenUtils.getUserId(refreshTokensParams.getRefreshToken());
        return userRepository.findById(userId).map(this::createFreshTokens).orElseThrow(
                () -> new IllegalArgumentException("User with id " + userId + " does not exist")
        );
    }

    @Override
    public boolean isTokenValid(final String token) {
        return tokenUtils.isTokenValid(token);
    }

    @Override
    public AuthTokens createLoginTokens(final UserCredentials userCredentials) {
        final Optional<User> userOptional = userRepository.findByPhone(userCredentials.getPhone());
        return userOptional.map(this::createFreshTokens)
                .orElseThrow(
                        () -> new IllegalArgumentException("User with phone " + userCredentials.getPhone() + " does not exist")
                );
    }

    @Override
    public boolean isCredentialsValid(final UserCredentials userCredentials) {
        final Optional<User> userOptional = userRepository.findByPhone(userCredentials.getPhone());
        return userOptional.map(
                user -> passwordEncoder.matches(userCredentials.getPassword(), user.getHashPassword())
        ).orElse(false);
    }

    @Override
    public EntryProcessMarker serveEntryProcess(final AppEntryPoint appEntryPoint) {
        final String phone = appEntryPoint.getPhone();
        if (isUserExistsByPhone(phone)) {
            return entryProcessMarkerFactory.createLoginMarker();
        }
        return startRegistrationProcess(phone);
    }

    @Override
    public PhoneCheckProcessMarker validatePhoneCheckCode(final PhoneCheckCodeParams phoneCheckCodeParams) {
        final Optional<PhoneCheckCode> phoneCheckCode = phoneCheckCodeRepository.findByRegistrationSessionId(
                phoneCheckCodeParams.getRegistrationSessionId()
        );
        final String registrationSessionId = phoneCheckCodeParams.getRegistrationSessionId();
        if (!phoneCheckCode.isPresent()) {
            return phoneCheckProcessMarkerFactory.createMarkerForInvalidCode(registrationSessionId);
        }
        final PhoneCheckCode currentCode = phoneCheckCodeRepository.save(
                phoneCheckCodeFactory.updatePhoneCheckCode(phoneCheckCode.get())
        );
        if (PhoneCheckCodeStatus.BLOCKED.equals(currentCode.getStatus())) {
            return phoneCheckProcessMarkerFactory.createMarkerForFailedAttemptLimit(registrationSessionId);
        }
        if (isCodeInvalid(phoneCheckCodeParams, currentCode)) {
            return phoneCheckProcessMarkerFactory.createMarkerForInvalidCode(registrationSessionId);
        }
        if (isCodeExpired(currentCode)) {
            return phoneCheckProcessMarkerFactory.createMarkerForFailedExpTime(registrationSessionId);
        }
        currentCode.setStatus(PhoneCheckCodeStatus.USED);
        phoneCheckCodeRepository.save(currentCode);
        entryControlRepository.save(checkEntryControlPhone(currentCode.getPhone()));
        return phoneCheckProcessMarkerFactory.createMarkerForSuccessfulCheck(registrationSessionId);
    }

    @Override
    public User registerUser(final RegistrationParams registrationParams) {
        if (isUserCreationAllowed(registrationParams)) {
            final User user = new User();
            user.setPhone(registrationParams.getPhone());
            user.setHashPassword(passwordEncoder.encode(registrationParams.getPassword()));
            user.setStatus(UserStatus.ACTIVE);
            user.setPremiumStatus(UserPremiumStatus.AVAILABLE);
            userRepository.save(user);

            final BusinessCard businessCard = new BusinessCard();
            businessCard.setUserId(user.getId());
            businessCard.setName(registrationParams.getName());
            businessCard.setSurname(registrationParams.getSurname());
            businessCard.setPhone(registrationParams.getPhone());
            businessCard.setPremium(false);
            businessCardRepository.save(businessCard);

            return user;
        }
        return null;
    }

    private EntryControl checkEntryControlPhone(final String phone) {
        final EntryControl entryControl = entryControlRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Entry Control does not exist."));
        entryControl.setStatus(EntryControlStatusEnum.CHECKED);
        return entryControl;
    }

    private boolean isUserExistsByPhone(final String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    private EntryProcessMarker startRegistrationProcess(final String phone) {
        final EntryControl entryControl = getUpdatedEntryControl(phone);
        if (EntryControlStatusEnum.BANNED.equals(entryControl.getStatus())) {
            return entryProcessMarkerFactory.createMarkerForBannedPhone();
        }
        if (EntryControlStatusEnum.CHECKED.equals(entryControl.getStatus())) {
            final PhoneCheckCode phoneCheckCodeForCheckedPhone = phoneCheckCodeFactory.createCode(phone);
            phoneCheckCodeRepository.save(phoneCheckCodeForCheckedPhone);
            return entryProcessMarkerFactory.createMarkerForCheckedPhone(phoneCheckCodeForCheckedPhone);
        }
        final PhoneCheckCode phoneCheckCode = phoneCheckCodeFactory.createCode(phone);
        final boolean isSmsSent = smsUtils.sendSmsWithCode(phoneCheckCode.getPhone(), phoneCheckCode.getCode());
        if (isSmsSent) {
            phoneCheckCodeRepository.save(phoneCheckCode);
            return entryProcessMarkerFactory.createMarkerForSuccessfulCodeSending(phoneCheckCode);
        } else {
            return entryProcessMarkerFactory.createMarkerForFailedCodeSending();
        }
    }

    private EntryControl getUpdatedEntryControl(final String phone) {
        EntryControl updatedEntryControl;
        final Optional<EntryControl> entryControlOptional = entryControlRepository.findByPhone(phone);
        if (entryControlOptional.isPresent()) {
            updatedEntryControl = entryControlFactory.updateEntryControl(entryControlOptional.get());
        } else {
            updatedEntryControl = entryControlFactory.createEntryControl(phone);
        }
        return entryControlRepository.save(updatedEntryControl);
    }

    private AuthTokens createFreshTokens(final User user) {
        final AuthTokens authTokens = new AuthTokens();
        authTokens.setUserId(user.getId());
        authTokens.setAccessToken(tokenUtils.createAccessToken(user));
        authTokens.setRefreshToken(tokenUtils.createRefreshToken(user));
        return authTokens;
    }

    private boolean isCodeInvalid(final PhoneCheckCodeParams phoneCheckCodeParams, final PhoneCheckCode code) {
        return !phoneCheckCodeParams.getCode().equals(code.getCode()) ||
                !phoneCheckCodeParams.getPhone().equals(code.getPhone()) ||
                PhoneCheckCodeStatus.USED.equals(code.getStatus());
    }

    private boolean isCodeExpired(final PhoneCheckCode code) {
        return code.getExpirationTime() < System.currentTimeMillis();
    }

    private boolean isUserCreationAllowed(final RegistrationParams registrationParams) {
        final String phone = registrationParams.getPhone();
        final String registrationSessionId = registrationParams.getRegistrationSessionId();
        final EntryControl entryControl = entryControlRepository.findByPhone(phone)
                .orElseThrow(() -> new IllegalArgumentException("Entry Control does not exist."));
        final PhoneCheckCode phoneCheckCode = phoneCheckCodeRepository.findByRegistrationSessionId(registrationSessionId)
                .orElseThrow(() -> new IllegalArgumentException("Phone Check Code does not exist."));
        return !isUserExistsByPhone(phone) && EntryControlStatusEnum.CHECKED.equals(entryControl.getStatus())
                && phoneCheckCode.getPhone().equals(phone) && registrationParams.getPassword().equals(registrationParams.getPasswordCheck());
    }
}
