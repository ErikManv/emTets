package ru.test.alfa.account;

import org.springframework.stereotype.Service;
import ru.test.alfa.account.dto.TransferRequest;

@Service
public interface AccountService {

    Account save(Account account);

    void transferFounds(TransferRequest request);
}
