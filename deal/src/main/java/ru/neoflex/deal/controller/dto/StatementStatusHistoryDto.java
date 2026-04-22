package ru.neoflex.deal.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.dictionary.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "История статусов заявки")
public class StatementStatusHistoryDto {

    @Schema(description = "Статус заявки", example = "PREAPPROVAL")
    private ApplicationStatus status;

    @Schema(description = "Время изменения", example = "2023-10-15T12:00:00")
    private LocalDateTime time;

    @Schema(description = "Тип изменения статуса", example = "AUTOMATIC")
    private ChangeType changeType;

}
