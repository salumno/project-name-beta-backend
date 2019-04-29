package ru.project.around.util.factory;

import org.springframework.stereotype.Component;
import ru.project.around.model.marker.PhoneCheckProcessMarker;

@Component
public class PhoneCheckProcessMarkerFactory {
    public PhoneCheckProcessMarker createMarkerForSuccessfulCheck(final String registrationSessionId) {
        final PhoneCheckProcessMarker phoneCheckProcessMarker = createPlainMarker(registrationSessionId);
        phoneCheckProcessMarker.setCodeChecked(true);
        return phoneCheckProcessMarker;
    }

    public PhoneCheckProcessMarker createMarkerForInvalidCode(final String registrationSessionId) {
        final PhoneCheckProcessMarker phoneCheckProcessMarker = createPlainMarker(registrationSessionId);
        phoneCheckProcessMarker.setCodeChecked(false);
        phoneCheckProcessMarker.setCodeInvalid(true);
        return phoneCheckProcessMarker;
    }

    public PhoneCheckProcessMarker createMarkerForFailedExpTime(final String registrationSessionId) {
        final PhoneCheckProcessMarker phoneCheckProcessMarker = createPlainMarker(registrationSessionId);
        phoneCheckProcessMarker.setCodeChecked(false);
        phoneCheckProcessMarker.setExpirationTimeOver(true);
        return phoneCheckProcessMarker;
    }

    public PhoneCheckProcessMarker createMarkerForFailedAttemptLimit(final String registrationSessionId) {
        final PhoneCheckProcessMarker phoneCheckProcessMarker = createPlainMarker(registrationSessionId);
        phoneCheckProcessMarker.setCodeChecked(false);
        phoneCheckProcessMarker.setAttemptLimitOver(true);
        return phoneCheckProcessMarker;
    }

    private PhoneCheckProcessMarker createPlainMarker(final String registrationSessionId) {
        return PhoneCheckProcessMarker.builder()
                .registrationSessionId(registrationSessionId)
                .isCodeInvalid(false)
                .isAttemptLimitOver(false)
                .isExpirationTimeOver(false)
                .build();
    }
}
