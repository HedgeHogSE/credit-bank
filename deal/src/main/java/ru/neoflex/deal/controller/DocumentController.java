package ru.neoflex.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.deal.controller.dto.SesCodeDto;
import ru.neoflex.deal.service.DocumentService;

import java.util.UUID;

@RestController
@RequestMapping("/deal/document/{statementId}")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/send")
    public void sendDocuments(@PathVariable UUID statementId) {

        documentService.sendDocuments(statementId);
    }

    @PostMapping("/sign")
    public void createDocuments(@PathVariable UUID statementId) {

        documentService.sendSesCode(statementId);
    }

    @PostMapping("/code")
    public void signDocuments(@PathVariable UUID statementId, @RequestBody SesCodeDto sesCodeDto) {

        documentService.signDocuments(statementId, sesCodeDto.getSesCode());
    }
}
