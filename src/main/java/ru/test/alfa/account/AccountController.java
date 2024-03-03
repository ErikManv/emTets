package ru.test.alfa.account;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.test.alfa.account.dto.TransferRequest;

@Tag(name = "account_controller")
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
