package ru.project.around.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.around.model.entity.BusinessCardSnooper;

import java.util.List;

@Repository
public interface BusinessCardSnooperRepository extends JpaRepository<BusinessCardSnooper, Long> {
    List<BusinessCardSnooper> findAllBySnoopedUserId(final Long snoopedUserId);
}
