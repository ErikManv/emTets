package ru.test.alfa.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.test.alfa.user.User;

import java.time.Duration;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_gen")
    @SequenceGenerator(name = "account_id_gen", initialValue = 1000)
    private Long id;

    @Positive
    @Column(nullable = false)
    private double balance;

    @Positive
    @Column(name = "init_balance", nullable = false)
    private double initBalance;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final double rate = 0.05;

    @Column(name = "cap_period")
    private Duration capPeriod = Duration.ofMinutes(1);

    @Column(name = "cap_end")
    private boolean capitalizationEnd = false;

    @Column(name = "capitalization_constrain")
    private final double balanceCapConstrain = 2.07;

    public long calculateNumberOfPeriods() {
        return Math.round(Math.log(balanceCapConstrain)/Math.log(rate + 1)- 1);
    }
}
