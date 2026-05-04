package ru.neoflex.dossier.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.neoflex.dossier.enums.Theme;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessage {

    private String address;

    private Theme theme;

    private UUID statementId;

    private String text;

}
