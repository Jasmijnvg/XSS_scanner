package com.jasmi.xss_scanner.security;

import com.jasmi.xss_scanner.models.Role;
import com.jasmi.xss_scanner.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApiUserDetails implements UserDetails {
    private final User user;

//    private  String organisation = "";

    public ApiUserDetails(User user) {
        this.user = user;
    }

//    public ApiUserDetails(String userName, List<String> roles, String organisation) {
//        this.organisation = organisation;
//        user = new User();
//        user.setUserName(userName);
//
//        for (String role : roles) {
//            user.getRoles().add(new Role(role));
//        }
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = user.getRole();

        if (role != null && role.isActive()) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
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