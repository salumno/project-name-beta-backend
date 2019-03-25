package ru.project.around.repository;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.project.around.model.User;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT id, email, phone, hash_password FROM \"user\" WHERE email = $1")
    Mono<User> findUserByEmail(final String email);
}
