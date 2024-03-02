package ru.test.alfa.account;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.test.alfa.account.dto.TransferRequest;
import ru.test.alfa.exception.EmptyCredentials;
import ru.test.alfa.exception.InsufficientFounds;
import ru.test.alfa.user.User;
import ru.test.alfa.user.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

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

    @Scheduled(fixedRate = 60000)
    public void increaseBalance() {
        List<Account> accountList = accountRepository.findAllByCapitalizationEnd(false);
        if(!accountList.isEmpty()) {
            for (Account account : accountList) {
                if ((account.getBalance() * (1 + account.getRate())) / account.getInitBalance() <
                    account.getBalanceCapConstrain()) {
                    if(calculateSupposedInterestAccrual(account) == calculateActualInterestAccrual(account)) {
                        log.info("Пользователю {} начислено {}", account.getUser().getUsername(),
                            account.getBalance() * account.getRate());
                        account.setBalance(account.getBalance() * (1 + account.getRate()));
                        accountRepository.save(account);
                    } else {
                        long missedAccrualPeriods = calculateSupposedInterestAccrual(account) - calculateActualInterestAccrual(account);
                        account.setBalance(account.getBalance() * Math.pow ((1 + account.getRate()), missedAccrualPeriods));
                        log.info("Выполнен пересчет балансов на случай падения серверов");
                        accountRepository.save(account);
                    }
                } else {
                    account.setCapitalizationEnd(true);
                    accountRepository.save(account);
                    log.info("У пользователя {} достигнут лимит капитализации", account.getUser().getUsername());
                }

            }
        }
    }

    private User findUserByTransferRequest(TransferRequest request) {
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

    public long calculateActualInterestAccrual(Account account) {
        return Math.round(Math.log(account.getBalance()/account.getInitBalance())/Math.log(1 + account.getRate()));
    }

    public long calculateSupposedInterestAccrual(Account account) {
        Duration depositDuration = Duration.between(account.getCreationDate(), LocalDateTime.now());
        long periods = depositDuration.getSeconds() / account.getCapPeriod().getSeconds();
        return Math.min(periods, account.calculateNumberOfPeriods());
    }
}
