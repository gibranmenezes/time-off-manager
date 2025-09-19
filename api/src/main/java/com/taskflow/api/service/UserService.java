package com.taskflow.api.service;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.exception.ValidationException;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .accountExpired(!user.isActive())
                .accountLocked(!user.isActive())
                .credentialsExpired(!user.isActive())
                .disabled(!user.isActive())
                .build();

    }

    public User createUser(String username, String email, String password, Role role) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ValidationException("Username already taken", ValidationException.ValidationErrorType.INVALID_INPUT);
        }

        var user = User.builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, String username, String email, String password, Role role) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (email != null) {
            user.setEmail(email);
        }
        if (password != null) {
            user.setPassword(password);
        }
        if (role != null) {
            user.setRole(role);
        }

        return userRepository.save(user);
    }

    

}
