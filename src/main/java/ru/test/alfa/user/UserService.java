package ru.test.alfa.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.test.alfa.bankAccount.pojo.TransferRequest;
import ru.test.alfa.security.JwtService;
import ru.test.alfa.user.pojo.SearchRequest;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EntityManager em;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    public List<User> findAllByCriteria(SearchRequest searchRequest) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        List<Predicate> predicates = new ArrayList<>();

        Root<User> root = criteriaQuery.from(User.class);

        if(searchRequest.getFullName() != null) {
            Predicate fullNamePredicate = criteriaBuilder
                .like(criteriaBuilder.lower(root.get("fullName")), searchRequest.getFullName().toLowerCase() + "%");
            predicates.add(fullNamePredicate);
        }

        if(searchRequest.getEmail() != null) {
            Predicate emailPredicate = criteriaBuilder
                .equal(criteriaBuilder.lower(root.get("email")), searchRequest.getEmail().toLowerCase());
            predicates.add(emailPredicate);
        }

        if(searchRequest.getPhoneNumber() != null) {
            Predicate phonePredicate = criteriaBuilder
                .equal(root.get("phoneNumber"), searchRequest.getPhoneNumber());
            predicates.add(phonePredicate);
        }

        if(searchRequest.getBirthday() != null) {
            Predicate birthdayPredicate = criteriaBuilder
                .greaterThan(root.get("dateOfBirth"), searchRequest.getBirthday());
            predicates.add(birthdayPredicate);
        }

        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        TypedQuery<User> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void addNumber(String token, String number) {
        User user = getByUsername(jwtService.extractUserName(token));
        user.getPhoneNumbers().add(number);
        userRepository.save(user);
    }

    public void updateNumber(String token, String oldNumber, String newNumber) {
        User user = getByUsername(jwtService.extractUserName(token));
        deleteNumber(token, oldNumber);
        user.getPhoneNumbers().add(newNumber);
        userRepository.save(user);
    }

    public void deleteNumber(String token, String number) {
        User user = getByUsername(jwtService.extractUserName(token));
        if(user.getPhoneNumbers().size() > 1) {
            user.getPhoneNumbers().remove(number);
        }
        userRepository.save(user);
    }

    public void addEmail(String token, String email) {
        User user = getByUsername(jwtService.extractUserName(token));
        user.getEmail().add(email);
        userRepository.save(user);
    }

    public void updateEmail(String token, String oldEmail, String newEmail) {
        User user = getByUsername(jwtService.extractUserName(token));
        deleteNumber(token, oldEmail);
        user.getEmail().add(newEmail);
        userRepository.save(user);
    }

    public void deleteEmail(String token, String email) {
        User user = getByUsername(jwtService.extractUserName(token));
        if(user.getEmail().size() > 1) {
            user.getEmail().remove(email);
        }
        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

//    public boolean userExist(TransferRequest request) {
//        System.out.println(userRepository.findByEmail(request.getEmail()));
//        return userRepository.findByEmail(request.getEmail()).isEmpty();
//    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }
}