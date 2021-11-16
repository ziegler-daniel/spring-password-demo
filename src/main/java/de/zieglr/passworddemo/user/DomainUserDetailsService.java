package de.zieglr.passworddemo.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;

@Service
@Slf4j
@AllArgsConstructor
public class DomainUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(this::mapUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return userRepository.findByUsername(user.getUsername())
                .map(storedUser -> {
                    log.info("Update stored password for user {}. Old hash {} new hash {}.", user.getUsername(),
                            storedUser.getPasswordHash(), newPassword);

                    storedUser.setPasswordHash(newPassword);
                    storedUser.setLastPasswordChange(Instant.now());
                    userRepository.save(storedUser);
                    return mapUser(storedUser);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + user.getUsername()));
    }

    private org.springframework.security.core.userdetails.User mapUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(), new ArrayList<>());
    }
}
