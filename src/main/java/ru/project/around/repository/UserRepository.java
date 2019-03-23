package ru.project.around.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.project.around.model.User;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Mono<User> findUserByEmail(@Param("email") final String email);
}
