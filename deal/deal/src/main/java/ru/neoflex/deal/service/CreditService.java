package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.CreditDto;
import ru.neoflex.deal.entity.Credit;
import ru.neoflex.deal.enums.CreditStatus;
import ru.neoflex.deal.repository.CreditRepository;

@Service
@RequiredArgsConstructor
public class CreditService {

    private final CreditRepository creditRepository;

    public void createCredit(CreditDto creditDto) {

        Credit credit = Credit
                .builder()
                .amount(creditDto.getAmount())
                .term(creditDto.getTerm())
                .monthlyPayment(creditDto.getMonthlyPayment())
                .rate(creditDto.getRate())
                .psk(creditDto.getPsk())
                .paymentSchedule(creditDto.getPaymentSchedule())
                .insuranceEnabled(creditDto.getIsInsuranceEnabled())
                .salaryClient(creditDto.getIsSalaryClient())
                .creditStatus(CreditStatus.CALCULATED)
                .build();

        creditRepository.save(credit);
    }
}
