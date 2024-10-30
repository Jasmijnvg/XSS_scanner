package com.jasmi.xss_scanner.security;

import com.jasmi.xss_scanner.models.Role;
import com.jasmi.xss_scanner.models.User;
import com.jasmi.xss_scanner.repositories.RoleRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class ApiUserDetails implements UserDetails {
    private final User user;

    public ApiUserDetails(User user) {
        this.user = user;
    }

    public ApiUserDetails(String userName, List<String> roles) {
        user = new User();
        user.setUserName(userName);

        for (String role : roles) {
            user.getRoles().add(new Role(role));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role: user.getRoles()) {
            if(role.isActive()) {
                authorities.add(new SimpleGrantedAuthority(String.valueOf(role.getRoleName())));
            }
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return  user.getId() + "::" + user.getUserName();
    }

//    @Override
//    public boolean isAccountNonExpired() {
//        return !user.isExpired();
//    }
//    @Override
//    public boolean isAccountNonLocked() {
//        return !user.isLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return !user.areCredentialsExpired();
//    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}