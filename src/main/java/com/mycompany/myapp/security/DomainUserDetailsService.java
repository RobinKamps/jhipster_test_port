package com.mycompany.myapp.security;

import com.mycompany.myapp.domain.Authority;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        log.debug("Authenticating {}", email);
        Optional<User> userByEmailFromDatabase = userRepository.findOneWithAuthoritiesByEmail(email.toLowerCase());
        if (userByEmailFromDatabase.isPresent()) {
            return userByEmailFromDatabase.get();
        }
        throw (new UsernameNotFoundException("User with " + email + " was not found in the database"));
    }

    private User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        Set<Authority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new Authority(authority.getAuthority()))
            .collect(Collectors.toSet());
        User domainUser = new User();
        domainUser.setEmail(user.getEmail());
        domainUser.setPassword(user.getPassword());
        domainUser.setAuthorities(grantedAuthorities);

        return domainUser;
    }
}
