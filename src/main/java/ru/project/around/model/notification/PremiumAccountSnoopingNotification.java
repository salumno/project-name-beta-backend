package ru.project.around.model.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PremiumAccountSnoopingNotification {
    private Long snoopedUserId;

    private Long snooperId;
}
