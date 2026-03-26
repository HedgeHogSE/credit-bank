package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.dto.LoanStatementRequestDto;
import ru.neoflex.deal.entity.Client;
import ru.neoflex.deal.json.Passport;
import ru.neoflex.deal.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client createClient(LoanStatementRequestDto request) {

        Client client = Client
                .builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .passport(Passport
                        .builder()
                        .series(request.getPassportSeries())
                        .number(request.getPassportNumber())
                        .build())
                .build();

        return clientRepository.save(client);
    }
}
