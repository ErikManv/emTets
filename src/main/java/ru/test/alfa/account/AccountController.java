package ru.test.alfa.account;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.test.alfa.account.dto.TransferRequest;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/sendMoney")
    public  void moneyTransfer(@RequestBody TransferRequest request) {
        accountService.transferFounds(request);
    }
}
