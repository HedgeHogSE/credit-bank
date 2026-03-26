package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.deal.entity.Statement;

import java.util.UUID;

@Repository
public interface StatementRepository extends JpaRepository<Statement, Long> {
    Statement getStatementByStatementId(UUID statementId);
}
