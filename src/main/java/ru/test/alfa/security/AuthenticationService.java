package ru.test.alfa.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.test.alfa.account.Account;
import ru.test.alfa.account.AccountService;
import ru.test.alfa.security.requestDTO.SignInRequest;
import ru.test.alfa.security.requestDTO.SignUpRequest;
import ru.test.alfa.user.Role;
import ru.test.alfa.user.User;
import ru.test.alfa.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        Account account = new Account();
        account.setInitBalance(request.getInitBalance());
        account.setBalance(request.getInitBalance());
        account.setCreationDate(LocalDateTime.now());

        User user = User.builder()
            .username(request.getUsername())
            .email(List.of(request.getEmail()))
            .password(passwordEncoder.encode(request.getPassword()))
            .phoneNumbers(List.of(request.getPhoneNumber()))
            .role(Role.USER)
            .dateOfBirth(request.getDateOfBirth())
            .fullName(request.getFullName())
            .build();

        user.setAccount(accountService.save(account));
        account.setUser(userService.save(user));

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        userService.getByUsername(request.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        ));

        UserDetails user = userService
            .userDetailsService()
            .loadUserByUsername(request.getUsername());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
