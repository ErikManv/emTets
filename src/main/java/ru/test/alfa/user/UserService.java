package ru.test.alfa.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.test.alfa.exception.EmailNotFoundException;
import ru.test.alfa.exception.PhoneNumberNotFoundException;
import ru.test.alfa.exception.UserNotFoundException;
import ru.test.alfa.user.dto.SearchRequest;
import ru.test.alfa.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final EntityManager em;
    private final UserRepository userRepository;

    public List<UserDto> findAllByCriteria(Integer pageNumber,
                                           Integer limit,
                                           SearchRequest searchRequest,
                                           boolean ascending) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        List<Predicate> predicates = new ArrayList<>();

        int offset = (pageNumber - 1) * limit;

        Root<User> root = criteriaQuery.from(User.class);

        if(searchRequest.getFullName() != null) {
            if(ascending) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("fullName")));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("fullName")));
            }
            Predicate fullNamePredicate = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("fullName")), searchRequest.getFullName().toLowerCase() + "%");
            log.info("поиск по ФИО: {}", searchRequest.getFullName());
            predicates.add(fullNamePredicate);
        }

        if(searchRequest.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder
                .equal(criteriaBuilder.lower(root.get("email")), searchRequest.getEmail().toLowerCase());
            log.info("поиск по email: {}", searchRequest.getEmail());
            predicates.add(emailPredicate);
        }

        if(searchRequest.getPhoneNumber() != null) {
            Predicate phonePredicate = criteriaBuilder
                .isMember(searchRequest.getPhoneNumber(), root.get("phoneNumbers"));
            log.info("поиск по номеру телефона: {}", searchRequest.getPhoneNumber());
            predicates.add(phonePredicate);
        }

        if(searchRequest.getBirthday() != null) {
            if(ascending) {
                criteriaQuery.orderBy(criteriaBuilder.asc(root.get("dateOfBirth")));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("dateOfBirth")));
            }
            Predicate birthdayPredicate = criteriaBuilder
                .greaterThan(root.get("dateOfBirth"), searchRequest.getBirthday());
            log.info("поиск по дате рождения: {}", searchRequest.getBirthday());
            predicates.add(birthdayPredicate);
        }

        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        TypedQuery<User> query = em.createQuery(criteriaQuery);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList().stream().map(this::userToDto).toList();
    }

    public User save(User user) {
        log.info("пользователь {} сохранен", user.getUsername());
        return userRepository.save(user);
    }

    public void addNumber(String number) {
        User user = getCurrentUser();
        user.getPhoneNumbers().add(number);
        log.info("номер телефона {} добавлен", number);
        userRepository.save(user);
    }

    public void updateNumber(String oldNumber, String newNumber) {
        User user = getCurrentUser();
        if(user.getPhoneNumbers().contains(oldNumber)){
            deleteNumber(oldNumber);
            user.getPhoneNumbers().add(newNumber);
            log.info("номер телефона {} изменен на {}", oldNumber, newNumber);
            userRepository.save(user);
        } else {
            throw new PhoneNumberNotFoundException(oldNumber);
        }
    }

    public void deleteNumber(String number) {
        User user = getCurrentUser();
        if(user.getPhoneNumbers().size() > 1 && user.getPhoneNumbers().contains(number)) {
            user.getPhoneNumbers().remove(number);
            log.info("номер телефона {} удален", number);
            userRepository.save(user);
        } else {
            throw new PhoneNumberNotFoundException(number);
        }
    }

    public void addEmail(String email) {
        User user = getCurrentUser();
        user.getEmail().add(email);
        log.info("email {} добавлен", email);
        userRepository.save(user);
    }

    public void updateEmail(String oldEmail, String newEmail) {
        User user = getCurrentUser();
        if(user.getEmail().contains(oldEmail)){
            deleteEmail(oldEmail);
            user.getEmail().add(newEmail);
            log.info("email {} изменен на {}", oldEmail, newEmail);
            userRepository.save(user);
        } else {
            throw new PhoneNumberNotFoundException(oldEmail);
        }
    }

    public void deleteEmail(String email) {
        User user = getCurrentUser();
        if(user.getEmail().size() > 1 && user.getEmail().contains(email)) {
            user.getEmail().remove(email);
            log.info("email {} удален", email);
            userRepository.save(user);
        } else {
            throw new EmailNotFoundException(email);
        }
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumbers(phoneNumber)
            .orElseThrow(() -> new UserNotFoundException(phoneNumber));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    private UserDto userToDto(User user) {
        return UserDto.builder()
            .balance(user.getAccount().getBalance())
            .dateOfBirth(user.getDateOfBirth())
            .phoneNumbers(user.getPhoneNumbers())
            .fullName(user.getFullName())
            .username(user.getUsername())
            .email(user.getEmail())
            .build();
    }
}