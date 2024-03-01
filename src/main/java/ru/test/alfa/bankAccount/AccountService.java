package ru.test.alfa.bankAccount;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.test.alfa.bankAccount.pojo.TransferRequest;
import ru.test.alfa.security.JwtService;
import ru.test.alfa.user.User;
import ru.test.alfa.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final JwtService jwtService;


    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public void transferFounds(String token, TransferRequest request) {

        User sender = userService.getByUsername(jwtService.extractUserName(token));
        User recipient = userService.getByUsername(request.getUsername());

        double senderBalance = sender.getAccount().getBalance();
        double recipientBalance = recipient.getAccount().getBalance();

        if(sender.getAccount().getBalance() - request.getAmount() > 0) {
            sender.getAccount().setBalance(senderBalance - request.getAmount());
            recipient.getAccount().setBalance(recipientBalance + request.getAmount());
            userService.save(sender);
            userService.save(recipient);
        }
    }

    @Scheduled(fixedRate = 60000)
    public void increaseBalance() {
        List<Account> accountList = accountRepository.findAll();
        if(!accountList.isEmpty()) {
            for (Account account : accountList) {
                if ((account.getBalance() * (1 + account.getRate())) / account.getInitBalance() <
                    account.getBalanceCapConstrain()) {
                    account.setBalance(account.getBalance() * (1 + account.getRate()));
                    accountRepository.save(account);
                } else {
                    return;
                }
            }
        } else {
            return;
        }
    }
}
