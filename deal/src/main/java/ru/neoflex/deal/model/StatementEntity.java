package ru.neoflex.deal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "statement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class StatementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "statement_id")
    private UUID statementId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "client_id")
    @NotNull
    private ClientEntity clientEntity;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_id", referencedColumnName = "credit_id")
    private CreditEntity creditEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @NotNull
    private ApplicationStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "applied_offer")
    private LoanOffer appliedOffer;

    @Column(name = "sign_date")
    private LocalDateTime signDate;

    @Column(name = "ses_code")
    private String sesCode;

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "status_history")
    @NotNull
    private List<StatusHistory> statusHistory = new ArrayList<>();
}