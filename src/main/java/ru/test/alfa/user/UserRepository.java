package ru.test.alfa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE :email IN elements(u.email)")
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE :phoneNumber IN elements(u.phoneNumbers)")
    Optional<User> findByPhoneNumbers(String phoneNumber);
}
