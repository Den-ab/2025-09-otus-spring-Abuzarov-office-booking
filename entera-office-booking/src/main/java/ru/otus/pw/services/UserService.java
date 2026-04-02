package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.EnteraUser;
import ru.otus.pw.repositories.EnteraUserRepository;

import java.util.List;

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

    //endregion
}
