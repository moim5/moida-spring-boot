package com.kh.moida.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
public class UserPrincipal implements UserDetails {
    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = Objects.equals(user.getIsAdmin(), "Y") ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(user.getIsActive(), "Y");
    }

    public String getGender() {
        return user.getGender();
    }

    public String getZipCode() {
        return user.getZipCode();
    }

    public String getAddress1() {
        return user.getAddress1();
    }

    public String getAddress2() {
        return user.getAddress2();
    }

    public String getPhone() {
        return user.getPhone();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getName() {
        return user.getName();
    }

    public String getIsAdmin() {
        return user.getIsAdmin();
    }

    public String getIsActive() {
        return user.getIsActive();
    }

    public Date getBirthday() {
        return user.getBirthday();
    }
}
