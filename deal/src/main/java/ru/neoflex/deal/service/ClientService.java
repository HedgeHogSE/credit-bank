package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;
import ru.neoflex.deal.service.command.FinishRegistrationRequestCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity createClient(ClientEntity clientEntity) {
        log.info("Creating new client: {}", clientEntity.getFirstName());
        return clientRepository.save(clientEntity);
    }

    public void complementClient(ClientEntity clientEntity, FinishRegistrationRequestCommand request) {
        clientEntity.setGender(request.getGender());
        clientEntity.setDependentAmount(request.getDependentAmount());
        clientEntity.setMaritalStatus(request.getMaritalStatus());
        clientEntity.getPassport().setIssueBranch(request.getPassportIssueBranch());
        clientEntity.getPassport().setIssueDate(request.getPassportIssueDate());
        clientEntity.setEmployment(request.getEmployment());
        clientEntity.setAccountNumber(request.getAccountNumber());
    }
}
