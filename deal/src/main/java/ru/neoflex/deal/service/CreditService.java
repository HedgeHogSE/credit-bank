package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.model.CreditEntity;
import ru.neoflex.deal.model.dictionary.CreditStatus;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.service.command.CreditCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditService {

    private final CreditRepository creditRepository;

    public CreditEntity createCredit(CreditCommand creditCommand) {
        log.info("Creating credit with amount: {}", creditCommand.getAmount());
        CreditEntity creditEntity = CreditEntity
                .builder()
                .amount(creditCommand.getAmount())
                .term(creditCommand.getTerm())
                .monthlyPayment(creditCommand.getMonthlyPayment())
                .rate(creditCommand.getRate())
                .psk(creditCommand.getPsk())
                .paymentSchedule(creditCommand.getPaymentSchedule())
                .insuranceEnabled(creditCommand.getIsInsuranceEnabled())
                .salaryClient(creditCommand.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build();

       return creditRepository.save(creditEntity);
    }
}
