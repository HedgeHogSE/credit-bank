package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.controller.dto.LoanStatementRequestDto;
import ru.neoflex.deal.controller.dto.StatementDto;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatementMapper {

    LoanStatementRequestCommand toLoanStatementRequestCommand(LoanStatementRequestDto requestDto);

    LoanStatementRequestDto toLoanStatementRequestDto(LoanStatementRequestCommand requestCommand);

    LoanOffer toLoanOffer(LoanOfferDto requestDto);

    StatementDto toStatementDto(StatementEntity statementEntity);

    List<StatementDto> toListStatementDto(List<StatementEntity> statementEntities);

}
