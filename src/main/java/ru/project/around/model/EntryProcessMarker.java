package ru.project.around.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntryProcessMarker {
    private EntryProcessEnum process;
    private boolean isProcessAvailable;
    private boolean isCodeSent;
    private boolean isPhoneChecked;
    private String registrationSessionId;
}