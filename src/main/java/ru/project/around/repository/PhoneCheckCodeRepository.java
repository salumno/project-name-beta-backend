package ru.project.around.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.around.model.entity.PhoneCheckCode;

import java.util.Optional;

@Repository
public interface PhoneCheckCodeRepository extends JpaRepository<PhoneCheckCode, Long> {
    Optional<PhoneCheckCode> findByRegistrationSessionId(final String registrationSessionId);
}
