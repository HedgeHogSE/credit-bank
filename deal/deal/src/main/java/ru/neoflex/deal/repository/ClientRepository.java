package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.deal.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
