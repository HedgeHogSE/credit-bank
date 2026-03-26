package ru.neoflex.deal.json;

import lombok.Builder;
import lombok.Data;
import ru.neoflex.deal.enums.ApplicationStatus;
import ru.neoflex.deal.enums.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
