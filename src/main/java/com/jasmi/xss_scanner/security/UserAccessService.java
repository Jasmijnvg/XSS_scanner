package com.jasmi.xss_scanner.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserAccessService {

    public boolean checkUserId(Authentication authentication, Long userId) {
        // Hier gaan we ervan uit dat ApiUserDetails je gebruiker ID geeft
        if (authentication.getPrincipal() instanceof ApiUserDetails userDetails) {
            // Controleer of de ingelogde gebruiker toegang heeft tot de gevraagde ID
            return userDetails.getId().equals(userId); // zorg ervoor dat je de ID kunt ophalen
        }
        return false; // Geen toegang
    }
}
