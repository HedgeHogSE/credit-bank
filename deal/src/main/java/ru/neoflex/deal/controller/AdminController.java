package ru.neoflex.deal.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.neoflex.deal.controller.dto.StatementDto;
import ru.neoflex.deal.mapper.StatementMapper;
import ru.neoflex.deal.service.StatementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal/admin/statement")
@RequiredArgsConstructor
@Tag(name = "Admin API", description = "Управление заявками")
public class AdminController {

    private final StatementService statementService;

    private final StatementMapper statementMapper;

    @GetMapping("/{id}")
    public StatementDto getStatementById(@PathVariable UUID id) {

        return statementMapper.toStatementDto(statementService.getStatementByStatementId(id));
    }

    @GetMapping
    public List<StatementDto> getStatements() {

        return statementMapper.toListStatementDto(statementService.getAllStatements());
    }
}
