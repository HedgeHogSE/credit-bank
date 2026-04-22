package ru.neoflex.deal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.deal.model.dictionary.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "credit")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "credit_id")
    private UUID creditId;

    @Column(name = "amount")
    @NotNull
    private BigDecimal amount;

    @Column(name = "term")
    @NotNull
    private Integer term;

    @Column(name = "monthly_payment")
    @NotNull
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    @NotNull
    private BigDecimal rate;

    @Column(name = "psk")
    @NotNull
    private BigDecimal psk;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payment_schedule")
    @NotNull
    private List<PaymentSchedule> paymentSchedule;

    @Column(name = "insurance_enabled")
    @NotNull
    private Boolean insuranceEnabled;

    @Column(name = "salary_client")
    @NotNull
    private Boolean salaryClient;

    @Enumerated(EnumType.STRING)
    @Column(name = "credit_status")
    @NotNull
    private CreditStatus creditStatus;
}
