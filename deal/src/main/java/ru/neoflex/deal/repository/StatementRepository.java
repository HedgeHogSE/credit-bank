package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.deal.model.StatementEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<StatementEntity, UUID> {
    Optional<StatementEntity> getStatementByStatementId(UUID statementId); // Optional
}
