package ru.neoflex.deal.model;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.dictionary.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
