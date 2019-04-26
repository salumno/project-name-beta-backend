package ru.project.around.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.around.model.entity.FavoriteCard;

import java.util.List;

@Repository
public interface FavoriteCardsRepository extends JpaRepository<FavoriteCard, Long> {
    List<FavoriteCard> findAllByUserId(final Long userId);

    FavoriteCard findByUserIdAndCardId(final Long userId, final Long cardId);
}
