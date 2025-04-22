package com.PoseidonCapitalSolutions.TradingApp.config;

import com.PoseidonCapitalSolutions.TradingApp.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * The type Custom user details.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer id;
    private final String username;
    private final String password;
    private final String role;

    /**
     * Instantiates a new Custom user details.
     *
     * @param user the user
     */
    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

}
