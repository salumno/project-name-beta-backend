package ru.project.around.util.factory;

import org.springframework.stereotype.Component;
import ru.project.around.model.status.EntryProcessEnum;
import ru.project.around.model.marker.EntryProcessMarker;
import ru.project.around.model.entity.PhoneCheckCode;

@Component
public class EntryProcessMarkerFactory {

    public EntryProcessMarker createLoginMarker() {
        return EntryProcessMarker.builder().process(EntryProcessEnum.LOGIN).isProcessAvailable(true).build();
    }

    public EntryProcessMarker createMarkerForBannedPhone() {
        return createRegistrationPlainMarker(false);
    }

    public EntryProcessMarker createMarkerForFailedCodeSending() {
        final EntryProcessMarker entryProcessMarker = createRegistrationPlainMarker(true);
        entryProcessMarker.setCodeSent(false);
        return entryProcessMarker;
    }

    public EntryProcessMarker createMarkerForSuccessfulCodeSending(PhoneCheckCode phoneCheckCode) {
        final EntryProcessMarker entryProcessMarker = createRegistrationPlainMarker(true);
        entryProcessMarker.setCodeSent(true);
        entryProcessMarker.setRegistrationSessionId(phoneCheckCode.getRegistrationSessionId());
        return entryProcessMarker;
    }

    public EntryProcessMarker createMarkerForCheckedPhone(PhoneCheckCode phoneCheckCode) {
        final EntryProcessMarker entryProcessMarker = createRegistrationPlainMarker(true);
        entryProcessMarker.setPhoneChecked(true);
        entryProcessMarker.setRegistrationSessionId(phoneCheckCode.getRegistrationSessionId());
        return entryProcessMarker;
    }

    private EntryProcessMarker createRegistrationPlainMarker(final boolean isProcessAvailable) {
        return EntryProcessMarker.builder().process(EntryProcessEnum.REGISTRATION).isProcessAvailable(isProcessAvailable).build();
    }
}
