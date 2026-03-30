package ru.neoflex.deal.controller.dto;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.dictionary.ApplicationStatus;
import ru.neoflex.deal.dictionary.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class StatementStatusHistoryDto {

    private ApplicationStatus status;

    private LocalDateTime time;

    private ChangeType changeType;

}
