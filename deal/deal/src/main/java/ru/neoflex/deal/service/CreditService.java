package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.model.CreditEntity;
import ru.neoflex.deal.dictionary.CreditStatus;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.service.command.CreditCommand;

@Service
@RequiredArgsConstructor
@Transactional
public class CreditService {

    private final CreditRepository creditRepository;

    public void createCredit(CreditCommand creditCommand) {

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

        creditRepository.save(creditEntity);
    }
}
