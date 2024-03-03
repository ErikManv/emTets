package ru.test.alfa.admin;

import ru.test.alfa.admin.dto.SignInRequest;
import ru.test.alfa.admin.dto.SignUpRequest;
import ru.test.alfa.security.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
