package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.EnteraUser;
import ru.otus.pw.repositories.EnteraUserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 */
@RequiredArgsConstructor
@Service
public class UserService {
    //region Fields

    /**
     * Репозиторий пользователей.
     */
    private final EnteraUserRepository userRepository;

    //endregion
    //region Public

    /**
     * Возвращает всех пользователей.
     *
     * @return Пользователи.
     */
    public List<EnteraUser> findAll() {

        return this.userRepository.findAll();
    }

    /**
     * Возвращает пользователя по логину.
     *
     * @param email Почта.
     *
     * @return Пользователь.
     */
    public Optional<EnteraUser> findByEmail(String email) {

        return this.userRepository.findByEmail(email);
    }

    //endregion
}
