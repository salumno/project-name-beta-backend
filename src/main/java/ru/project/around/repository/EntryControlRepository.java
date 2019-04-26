package ru.project.around.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.around.model.entity.EntryControl;

import java.util.Optional;

@Repository
public interface EntryControlRepository extends JpaRepository<EntryControl, Long> {
    Optional<EntryControl> findByPhone(final String phone);
}