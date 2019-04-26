package ru.project.around.util.factory;

import org.springframework.stereotype.Component;
import ru.project.around.model.entity.EntryControl;
import ru.project.around.model.status.EntryControlStatusEnum;

@Component
public class EntryControlFactory {
    private static final int INITIAL_COUNT_OF_REGISTRATION_ATTEMPT = 1;
    private static final int MAX_COUNT_OF_REGISTRATION_ATTEMPT = 5;

    public EntryControl createEntryControl(final String phone) {
        return EntryControl.builder().phone(phone).status(EntryControlStatusEnum.AVAILABLE)
                .registrationAttemptCount(INITIAL_COUNT_OF_REGISTRATION_ATTEMPT).build();
    }

    public EntryControl updateEntryControl(final EntryControl entryControl) {
        final int updatedAttemptCount = entryControl.getRegistrationAttemptCount() + 1;
        if (updatedAttemptCount > MAX_COUNT_OF_REGISTRATION_ATTEMPT) {
            entryControl.setStatus(EntryControlStatusEnum.BANNED);
        }
        entryControl.setRegistrationAttemptCount(updatedAttemptCount);
        return entryControl;
    }
}
