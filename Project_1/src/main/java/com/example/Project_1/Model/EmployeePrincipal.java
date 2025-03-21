package com.example.Project_1.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class EmployeePrincipal implements UserDetails {

    @Autowired
    public Employee2 employee2;

    public EmployeePrincipal(Employee2 employee2) {
        this.employee2 = employee2;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = employee2.getRole().toString();

        if (!role.startsWith("ROLE")) {
            role = "ROLE" + role;
        }
        System.out.println("DEBUG: Retrieved Role from DB → " + role);

        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return employee2.getPassword();
    }

    @Override
    public String getUsername() {
        return employee2.getUsername();
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
}
