package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.controllers.request_dtos.AreaDTO;
import ru.otus.pw.models.Area;
import ru.otus.pw.repositories.AreaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с пространствами.
 */
@RequiredArgsConstructor
@Service
public class AreaService {
    //region Fields

    /**
     * Репозиторий пространств.
     */
    private final AreaRepository areaRepository;

    //endregion
    //region Public

    /**
     * Возвращает список пространств.
     *
     * @return Список пространств.
     */
    public List<Area> findAll() {

        return this.areaRepository.findAll();
    }

    /**
     * Создает пространство.
     *
     * @param area Пространство.
     *
     * @return Созданное пространство.
     */
    public Area create(AreaDTO area) {

        return this.save(null, area.name());
    }

    /**
     * Обновляет пространство.
     *
     * @param area Пространство.
     *
     * @return Обновленное пространство.
     */
    public Area update(AreaDTO area) {

        final UUID areaId = UUID.fromString(area.id());

        return this.save(areaId, area.name());
    }

    /**
     * Удаляет пространство.
     *
     * @param areaId Идентификатор пространства.
     */
    public void delete(UUID areaId) {

        this.areaRepository.deleteById(areaId);
    }

    //endregion
    //region Private

    /**
     * Сохраняет пространство.
     * Если идентификатор нулловый, то значит будет создание пространства. Иначе - обновление.
     *
     * @param areaId Идентификатор пространства.
     * @param name Наименование.
     *
     * @return Сохраненное пространство.
     */
    private Area save(UUID areaId, String name) {

        return this.areaRepository.save(Area.builder().id(areaId).name(name).build());
    }

    //endregion
}
