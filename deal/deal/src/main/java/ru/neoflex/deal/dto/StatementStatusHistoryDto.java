package ru.neoflex.deal.dto;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class StatementStatusHistoryDto {

    private ApplicationStatus status;

    private LocalDateTime time;

    private ChangeType changeType;

}
