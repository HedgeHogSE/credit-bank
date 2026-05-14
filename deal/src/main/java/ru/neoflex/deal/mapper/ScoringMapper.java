package ru.neoflex.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import ru.neoflex.deal.controller.dto.FinishRegistrationRequestDto;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.service.command.FinishRegistrationRequestCommand;
import ru.neoflex.deal.service.command.ScoringDataCommand;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScoringMapper {

    @Mappings({
            @Mapping(source = "statement.appliedOffer.totalAmount", target = "amount"),
            @Mapping(source = "statement.appliedOffer.term", target = "term"),
            @Mapping(source = "statement.appliedOffer.isInsuranceEnabled", target = "isInsuranceEnabled"),
            @Mapping(source = "statement.appliedOffer.isSalaryClient", target = "isSalaryClient"),

            @Mapping(source = "statement.clientEntity.firstName", target = "firstName"),
            @Mapping(source = "statement.clientEntity.lastName", target = "lastName"),
            @Mapping(source = "statement.clientEntity.middleName", target = "middleName"),
            @Mapping(source = "statement.clientEntity.birthDate", target = "birthdate"),
            @Mapping(source = "statement.clientEntity.passport.series", target = "passportSeries"),
            @Mapping(source = "statement.clientEntity.passport.number", target = "passportNumber"),

            @Mapping(source = "request.passportIssueDate", target = "passportIssueDate"),
            @Mapping(source = "request.passportIssueBranch", target = "passportIssueBranch")
    })
    ScoringDataCommand toScoringData(FinishRegistrationRequestCommand request, StatementEntity statement);

    FinishRegistrationRequestCommand toFinishRegistrationRequestCommand(FinishRegistrationRequestDto requestDto);

}
