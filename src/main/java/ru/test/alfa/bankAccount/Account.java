package ru.test.alfa.bankAccount;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.scheduling.annotation.Scheduled;
import ru.test.alfa.user.User;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    @SequenceGenerator(name = "idgenerator", initialValue = 1000)
    private Long id;

    @Positive
    @Column(nullable = false)
    private double balance;

    @Positive
    @Column(name = "init_balance", nullable = false)
    private double initBalance;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private final double rate = 0.05;

    @Column(name = "capitalization_constrain")
    private final double balanceCapConstrain = 2.07;
}
