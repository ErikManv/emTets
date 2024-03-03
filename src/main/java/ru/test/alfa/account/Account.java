package ru.test.alfa.account;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import ru.test.alfa.user.User;

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
    @SequenceGenerator(name = "account_id_gen", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Positive
    @Column(name = "card_balance", nullable = false)
    private double cardBalance;

    @Positive
    @Column(nullable = false)
    private double deposit;

    @Positive
    @Column(name = "init_deposit", nullable = false)
    private double initDeposit;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final double rate = 0.05;

    @Column(name = "cap_end")
    private boolean capitalizationEnd = false;

    @Column(name = "cap_constrain")
    private final double depositCapConstrain = 2.07;

    public double calculateNumberOfPeriods() {
        return Math.log(deposit/initDeposit)/Math.log(rate + 1);
    }

    public double calculateMaxNumberOfPeriods() {
        return Math.log(depositCapConstrain)/Math.log(rate + 1);
    }
}
