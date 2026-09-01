package com.bridgelabz.employeemanagement.security;

import com.bridgelabz.employeemanagement.entity.User;
import com.bridgelabz.employeemanagement.enums.Role;
import com.bridgelabz.employeemanagement.enums.UserStatus;
import com.bridgelabz.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(
            OAuth2UserRequest userRequest) {

        OAuth2User oauth2User =
                super.loadUser(userRequest);

        String email =
                oauth2User.getAttribute("email");

        String name =
                oauth2User.getAttribute("name");

        String provider =
                userRequest
                        .getClientRegistration()
                        .getRegistrationId();

        if (email == null) {
            throw new RuntimeException(
                    "Email not found from OAuth2 provider"
            );
        }

        User user = userRepository
                .findByEmail(email)
                .orElseGet(() -> {

                    User newUser =
                            User.builder()
                                    .name(name)
                                    .email(email)
                                    .provider(provider)
                                    .role(Role.USER)
                                    .status(UserStatus.ACTIVE)
                                    .build();

                    return userRepository.save(newUser);
                });

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException(
                    "User account is inactive"
            );
        }

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                );

        return new DefaultOAuth2User(
                Collections.singleton(authority),
                oauth2User.getAttributes(),
                "email"
        );
    }
}