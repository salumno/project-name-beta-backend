package ru.project.around.util.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.project.around.model.entity.PhoneCheckCode;
import ru.project.around.model.status.PhoneCheckCodeStatus;

import java.util.UUID;

@Component
public class PhoneCheckCodeFactory {
    private static final int INITIAL_COUNT_OF_CHECK_ATTEMPT = 0;
    private static final int MAX_COUNT_OF_CHECK_ATTEMPT = 5;

    @Value("${app.registration.code.exp}")
    private long phoneCheckCodeExpirationTime;

    public PhoneCheckCode createCode(final String phone) {
        return PhoneCheckCode.builder()
                .registrationSessionId(getRegistrationSessionId())
                .code(getPhoneCheckCode())
                .phone(phone)
                .status(PhoneCheckCodeStatus.ACTIVE)
                .expirationTime(
                        System.currentTimeMillis() + phoneCheckCodeExpirationTime
                )
                .codeCheckAttemptCount(INITIAL_COUNT_OF_CHECK_ATTEMPT)
                .build();
    }

    public PhoneCheckCode updatePhoneCheckCode(final PhoneCheckCode phoneCheckCode) {
        final int updatedAttemptCount = phoneCheckCode.getCodeCheckAttemptCount() + 1;
        if (updatedAttemptCount > MAX_COUNT_OF_CHECK_ATTEMPT) {
            phoneCheckCode.setStatus(PhoneCheckCodeStatus.BLOCKED);
        }
        phoneCheckCode.setCodeCheckAttemptCount(updatedAttemptCount);
        return phoneCheckCode;
    }

    private String getPhoneCheckCode() {
        // TODO
        return getConstantCode();
    }

    private String getRegistrationSessionId() {
        return UUID.randomUUID().toString();
    }

    private String getConstantCode() {
        // TODO Remove after generateCode method will be implemented.
        return "54321";
    }
}
