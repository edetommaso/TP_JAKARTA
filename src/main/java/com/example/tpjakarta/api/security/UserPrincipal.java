package com.example.tpjakarta.api.security;

import java.security.Principal;
import java.util.Objects;

public class UserPrincipal implements Principal {
    private final String name;
    private final Long userId;

    public UserPrincipal(String name, Long userId) {
        if (name == null) throw new NullPointerException("illegal null input");
        this.name = name;
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }
    
    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(name, that.name) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userId);
    }

    @Override
    public String toString() {
        return "UserPrincipal{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
