package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.neoflex.deal.model.dictionary.CreditStatus;
import ru.neoflex.deal.model.CreditEntity;
import ru.neoflex.deal.repository.CreditRepository;
import ru.neoflex.deal.service.command.CreditCommand;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditServiceTest {

    @Mock
    private CreditRepository creditRepository;

    @Captor
    private ArgumentCaptor<CreditEntity> creditEntityCaptor;

    @InjectMocks
    private CreditService creditService;

    @Test
    void createCreditShouldCallRepositorySaveAndAddStatusCalculated() {

        CreditCommand creditCommand = createCreditCommand();

        creditService.createCredit(creditCommand);

        verify(creditRepository, times(1)).save(creditEntityCaptor.capture());

        CreditEntity savedEntity = creditEntityCaptor.getValue();

        assertEquals(CreditStatus.CALCULATED, savedEntity.getCreditStatus());

    }

    @Test
    void createCreditShouldThrowExceptionWhenDatabaseConflict() {
        CreditCommand creditCommand = createCreditCommand();
        creditCommand.setTerm(null);

        when(creditRepository.save(any(CreditEntity.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(DataIntegrityViolationException.class, () -> creditService.createCredit(creditCommand));

        verify(creditRepository, times(1)).save(any(CreditEntity.class));

    }

    private CreditCommand createCreditCommand() {

        return CreditCommand
                .builder()
                .amount(BigDecimal.valueOf(200000))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(18051.66))
                .psk(BigDecimal.valueOf(222222))
                .rate(BigDecimal.valueOf(0.15))
                .build();
    }
}
