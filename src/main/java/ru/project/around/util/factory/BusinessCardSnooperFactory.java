package ru.project.around.util.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.project.around.model.BusinessCardSnooper;

@Component
public class BusinessCardSnooperFactory {
    @Value("${app.premium.snooper.exp}")
    private long snooperExpirationTime;

    public BusinessCardSnooper createSnooper(final Long snoopedUserId, final Long snooperId) {
        return BusinessCardSnooper.builder()
                .snooperUserId(snooperId)
                .snoopedUserId(snoopedUserId)
                .expirationTime(System.currentTimeMillis() + snooperExpirationTime)
                .build();
    }
}
