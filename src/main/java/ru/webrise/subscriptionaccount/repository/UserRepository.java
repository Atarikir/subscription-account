package ru.webrise.subscriptionaccount.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.webrise.subscriptionaccount.model.User;


public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
