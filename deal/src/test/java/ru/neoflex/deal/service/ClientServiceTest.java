package ru.neoflex.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.neoflex.deal.model.dictionary.Gender;
import ru.neoflex.deal.model.dictionary.MaritalStatus;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.model.Passport;
import ru.neoflex.deal.repository.ClientRepository;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    void createClientShouldCallRepositorySaveAndReturnClientEntity() {

        ClientEntity clientEntity = createValidClientEntity();

        when(clientRepository.save(clientEntity)).thenReturn(clientEntity);

        clientService.createClient(clientEntity);

        verify(clientRepository, times(1)).save(clientEntity);
    }

    @Test
    void createClientShouldThrowExceptionWhenDatabaseConflict() {
        ClientEntity clientEntity = createValidClientEntity();
        clientEntity.setFirstName(null);

        when(clientRepository.save(any(ClientEntity.class)))
                .thenThrow(new DataIntegrityViolationException(""));

        assertThrows(DataIntegrityViolationException.class, () -> clientService.createClient(clientEntity));

        verify(clientRepository, times(1)).save(clientEntity);
    }

    private ClientEntity createValidClientEntity() {

        return ClientEntity
                .builder()
                .clientId(UUID.fromString("a4b81450-5241-476a-be05-0d43b836e66b"))
                .lastName("Ivanov")
                .firstName("Ivan")
                .middleName("Ivanovich")
                .birthDate(LocalDate.parse("1999-01-01"))
                .email("test@test.com")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(20000)
                .passport(Passport
                        .builder()
                        .series("6318")
                        .number("705726")
                        .build())
                .build();
    }
}
