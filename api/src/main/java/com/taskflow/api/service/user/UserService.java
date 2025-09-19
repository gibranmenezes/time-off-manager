package com.taskflow.api.service.user;

import com.taskflow.api.domain.enums.Role;
import com.taskflow.api.domain.user.User;
import com.taskflow.api.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String username, String email, String password, Role role) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
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
