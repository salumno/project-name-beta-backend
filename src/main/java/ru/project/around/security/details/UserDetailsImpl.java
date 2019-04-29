package ru.project.around.security.details;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.project.around.model.entity.User;
import ru.project.around.model.status.UserStatus;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private User user;

    public UserDetailsImpl(final User user) {
        this.user = user;
    }

    public UserDetailsImpl(final Long userId, final String phone) {
        user = User.builder().id(userId).phone(phone).status(UserStatus.ACTIVE).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getHashPassword();
    }

    @Override
    public String getUsername() {
        return user.getPhone();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getUserId() {
        return user.getId();
    }
}
