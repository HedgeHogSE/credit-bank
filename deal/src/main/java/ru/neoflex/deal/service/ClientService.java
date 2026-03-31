package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.repository.ClientRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity createClient(ClientEntity clientEntity) {
        log.info("Creating new client: {}", clientEntity.getFirstName());
        return clientRepository.save(clientEntity);
    }
}
