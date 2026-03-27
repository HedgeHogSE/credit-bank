package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.neoflex.deal.controller.dto.LoanOfferDto;
import ru.neoflex.deal.controller.dto.LoanStatementRequestDto;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.command.LoanStatementRequestCommand;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StatementMapper {

//    @Mapping(source = "clientEntity", target = "clientEntity")
//    @Mapping(source = "requestDto.amount", target = "creditEntity.amount")
//    @Mapping(source = "requestDto.term", target = "creditEntity.term")
//    StatementEntity toStatementEntity(LoanStatementRequestDto requestDto, ClientEntity clientEntity);

    LoanStatementRequestCommand toLoanStatementRequestCommand(LoanStatementRequestDto requestDto);

//    @Mapping(source = "clientEntity.firstName", target = "firstName")
//    @Mapping(source = "clientEntity.lastName", target = "lastName")
//    @Mapping(source = "clientEntity.middleName", target = "middleName")
//    @Mapping(source = "clientEntity.email", target = "email")
//    @Mapping(source = "clientEntity.birthDate", target = "birthDate")
//    @Mapping(source = "clientEntity.passport.series", target = "passportSeries")
//    @Mapping(source = "clientEntity.passport.number", target = "passportNumber")
//    @Mapping(source = "creditEntity.amount", target = "amount")
//    @Mapping(source = "creditEntity.term", target = "term")
//    LoanStatementRequestDto toLoanStatementRequestDto(StatementEntity statementEntity);


    LoanStatementRequestDto toLoanStatementRequestDto(LoanStatementRequestCommand requestCommand);


    LoanOffer toLoanOffer(LoanOfferDto requestDto);

}
