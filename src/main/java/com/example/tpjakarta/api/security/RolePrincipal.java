package com.example.tpjakarta.api.security;

import java.security.Principal;
import java.util.Objects;

public class RolePrincipal implements Principal {
    private final String name;

    public RolePrincipal(String name) {
        if (name == null) throw new NullPointerException("illegal null input");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RolePrincipal that = (RolePrincipal) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RolePrincipal{" +
                "name='" + name + '\'' +
                '}';
    }
}
