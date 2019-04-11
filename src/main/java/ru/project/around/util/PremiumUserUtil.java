package ru.project.around.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.around.model.User;
import ru.project.around.model.UserPremiumStatus;
import ru.project.around.repository.UserRepository;

@Component
public class PremiumUserUtil {

    private final UserRepository userRepository;

    @Autowired
    public PremiumUserUtil(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserPremium(final Long userId) {
        final User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User with id " + userId + " is not found.")
        );
        return UserPremiumStatus.ACTIVATED.equals(user.getPremiumStatus());
    }
}
