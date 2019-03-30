package ru.project.around.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.around.model.BusinessCard;

@Repository
public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {
    BusinessCard findByUserId(final Long userId);
}
