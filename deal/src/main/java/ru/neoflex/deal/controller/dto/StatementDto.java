package ru.neoflex.deal.controller.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.CreditEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatusHistory;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatementDto {

    private UUID statementId;

    private ClientEntity clientEntity;

    private CreditEntity creditEntity;

    private ApplicationStatus status;

    private LocalDateTime creationDate;

    private LoanOffer appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    private List<StatusHistory> statusHistory;

}
