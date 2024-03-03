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
import ru.test.alfa.user.UserServiceImpl;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserServiceImpl userService;

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void transferFounds(TransferRequest request) {

        User sender = userService.getCurrentUser();
        User recipient = findUserByTransferRequest(request);

        double senderBalance = sender.getAccount().getCardBalance();
        double recipientBalance = recipient.getAccount().getCardBalance();

        if(sender.getAccount().getCardBalance() - request.getAmount() > 0) {
            sender.getAccount().setCardBalance(senderBalance - request.getAmount());
            recipient.getAccount().setCardBalance(recipientBalance + request.getAmount());
            userService.save(sender);
            userService.save(recipient);
            log.info("Перевод суммы {} пользователю {} совершен", request.getAmount(), recipient.getUsername());
        } else {
            throw new InsufficientFounds(String.valueOf(sender.getAccount().getCardBalance()));
        }
    }

    @Scheduled(fixedRate = 60000)
    private void increaseBalance() {
        List<Account> accountList = accountRepository.findAllByCapitalizationEnd(false);
        if(!accountList.isEmpty()) {
            for (Account account : accountList) {
                if ((account.getDeposit() * (1 + account.getRate())) / account.getInitDeposit() <
                    account.getDepositCapConstrain()) {

                        log.info("Пользователю {} начислено {}", account.getUser().getUsername(),
                            account.getDeposit() * account.getRate());

                        account.setDeposit(account.getDeposit() * (1 + account.getRate()));
                        accountRepository.save(account);
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
}
