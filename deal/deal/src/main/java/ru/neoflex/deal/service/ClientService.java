package ru.neoflex.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.neoflex.deal.model.ClientEntity;
import ru.neoflex.deal.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientEntity createClient(ClientEntity clientEntity) {

        return clientRepository.save(clientEntity);
    }
}
