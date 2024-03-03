package ru.test.alfa.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.test.alfa.user.dto.SearchRequest;
import ru.test.alfa.user.dto.UserDto;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAllByCriteria(Integer pageNumber,
                                           Integer limit,
                                           SearchRequest searchRequest,
                                           boolean ascending);

    User save(User user);

    void addNumber(String number);

    void updateNumber(String oldNumber, String newNumber);

    void deleteNumber(String number);

    void addEmail(String email);

    void updateEmail(String oldEmail, String newEmail);

    void deleteEmail(String email);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    UserDetailsService userDetailsService();

    User getCurrentUser();
}
