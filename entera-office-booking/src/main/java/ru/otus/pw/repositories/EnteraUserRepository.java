package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.EnteraUser;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий пользователей.
 */
public interface EnteraUserRepository extends JpaRepository<EnteraUser, UUID> {
    //region Public

    /**
     * Возвращает пользователя по email.
     *
     * @param email Почта.
     *
     * @return Пользователь.
     */
    Optional<EnteraUser> findByEmail(String email);

    //endregion
}
