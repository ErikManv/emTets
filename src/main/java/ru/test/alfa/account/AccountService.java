package ru.test.alfa.account;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.test.alfa.account.requestDTO.TransferRequest;
import ru.test.alfa.exception.EmptyCredentials;
import ru.test.alfa.exception.InsufficientFounds;
import ru.test.alfa.security.JwtService;
import ru.test.alfa.user.User;
import ru.test.alfa.user.UserService;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AccountService.class);

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public void transferFounds(TransferRequest request) {

        User sender = userService.getCurrentUser();
        User recipient = findUserByTransferRequest(request);

        double senderBalance = sender.getAccount().getBalance();
        double recipientBalance = recipient.getAccount().getBalance();

        if(sender.getAccount().getBalance() - request.getAmount() > 0) {
            sender.getAccount().setBalance(senderBalance - request.getAmount());
            recipient.getAccount().setBalance(recipientBalance + request.getAmount());
            userService.save(sender);
            userService.save(recipient);
            log.info("Перевод суммы {} пользователю {} совершен", request.getAmount(), recipient.getUsername());
        } else {
            throw new InsufficientFounds(String.valueOf(sender.getAccount().getBalance()));
        }
    }

    @Scheduled(fixedRate = 1000)
    public void increaseBalance() {
        List<Account> accountList = accountRepository.findAll();
        if(!accountList.isEmpty()) {
            for (Account account : accountList) {
                if ((account.getBalance() * (1 + account.getRate())) / account.getInitBalance() <
                    account.getBalanceCapConstrain()) {
                    account.setBalance(account.getBalance() * (1 + account.getRate()));
                    accountRepository.save(account);
                }
            }
        }
    }

    public User findUserByTransferRequest(TransferRequest request) {
        if(request.getUsername() != null) {
            return userService.getByUsername(request.getUsername());
        } else if (request.getEmail() != null) {
            return userService.getByEmail(request.getEmail());
        } else if (request.getPhoneNumber() != null) {
            return userService.getByPhoneNumber(request.getPhoneNumber());
        } else {
            throw new EmptyCredentials();
        }
    }
}
