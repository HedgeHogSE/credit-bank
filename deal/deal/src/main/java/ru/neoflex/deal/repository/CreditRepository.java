package ru.neoflex.deal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neoflex.deal.entity.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
}
