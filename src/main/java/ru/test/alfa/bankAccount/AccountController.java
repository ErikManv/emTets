package ru.test.alfa.bankAccount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.test.alfa.bankAccount.pojo.TransferRequest;
import ru.test.alfa.security.*;
import ru.test.alfa.user.User;
import ru.test.alfa.user.UserService;

@RestController
@RequestMapping("/api/user/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    @PostMapping("/sendMoney")
    public  ResponseEntity<JwtAuthenticationResponse> signIn(@RequestHeader("Authorization") String authorizationHeader,
                                                             @RequestBody TransferRequest request) {
        String token = extractTokenFromHeader(authorizationHeader);
        accountService.transferFounds(token, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
