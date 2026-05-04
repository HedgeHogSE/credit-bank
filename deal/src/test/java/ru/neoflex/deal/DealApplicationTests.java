package ru.neoflex.deal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import ru.neoflex.deal.model.dictionary.ApplicationStatus;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.LoanOffer;
import ru.neoflex.deal.model.StatementEntity;
import ru.neoflex.deal.repository.StatementRepository;
import ru.neoflex.deal.service.StatementService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@ActiveProfiles("test")
class DealApplicationTests {

	@MockitoSpyBean
	private StatementService statementService;

	@Autowired
	private StatementRepository statementRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void secondMethodUpdateStatementCallShouldWaitFirstMethod() throws Exception {

		StatementEntity entity = StatementEntity.builder()
				.status(ApplicationStatus.PREAPPROVAL)
				.clientEntity(ClientEntity.builder()
						.firstName("Ivan")
						.lastName("Ivanov")
						.middleName("Ivanovich")
						.email("test@test.com")
						.birthDate(LocalDate.of(1990, 1, 1))
						.build())
				.statusHistory(new ArrayList<>())
				.build();

		entity = statementRepository.save(entity);
		UUID realId = entity.getStatementId();

		LoanOffer loanOffer = LoanOffer.builder()
				.statementId(realId)
				.term(12)
				.build();

		doAnswer(invocation -> {
			Object result = invocation.callRealMethod();

			Thread.sleep(1000);

			return result;
		}).when(statementService).getStatementByStatementId(realId);


		long startTime = System.currentTimeMillis();

		Thread t1 = new Thread(() -> statementService.updateStatement(loanOffer));
		Thread t2 = new Thread(() -> statementService.updateStatement(loanOffer));

		t1.start();
		t2.start();

		t1.join();
		t2.join();

		long duration = System.currentTimeMillis() - startTime;
		System.out.println("Общее время выполнения: " + duration + " ms");

		assertTrue(duration >= 2000);

		StatementEntity finalEntity = statementRepository.findById(realId).get();

		assertEquals(1, finalEntity.getStatusHistory().size());
	}

}
