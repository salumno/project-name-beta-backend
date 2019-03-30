package ru.project.around.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhoneCheckProcessMarker {
    private String registrationSessionId;
    private boolean isCodeChecked;
    private boolean isCodeInvalid;
    private boolean isAttemptLimitOver;
    private boolean isExpirationTimeOver;
}
