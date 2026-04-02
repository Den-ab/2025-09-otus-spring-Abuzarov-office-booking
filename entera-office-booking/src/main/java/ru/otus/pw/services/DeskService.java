package ru.otus.pw.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.controllers.request_dtos.DeskDTO;
import ru.otus.pw.models.Area;
import ru.otus.pw.models.Desk;
import ru.otus.pw.repositories.AreaRepository;
import ru.otus.pw.repositories.DeskRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы со столами.
 */
@RequiredArgsConstructor
@Service
public class DeskService {
    //region Fields

    /**
     * Репозиторий столов.
     */
    private final DeskRepository deskRepository;

    /**
     * Репозиторий пространств.
     */
    private final AreaRepository areaRepository;

    //endregion
    //region Public

    /**
     * Возвращает список столов.
     *
     * @return Список столов.
     */
    public List<Desk> findAll() {

        return this.deskRepository.findAll();
    }

    /**
     * Создает стол.
     *
     * @param deskDTO Данные стола.
     *
     * @return Созданный стол.
     */
    public Desk create(DeskDTO deskDTO) {

        final UUID areaId = UUID.fromString(deskDTO.areaId());
        final int number = deskDTO.number();

        return this.save(null, areaId, number);
    }

    /**
     * Обновляет стол.
     *
     * @param deskDTO Данные стола.
     *
     * @return Обновляет стол.
     */
    public Desk update(DeskDTO deskDTO) {

        final UUID areaId = UUID.fromString(deskDTO.areaId());
        final UUID deskId = UUID.fromString(deskDTO.id());
        final int number = deskDTO.number();

        return this.save(deskId, areaId, number);
    }

    /**
     * Удаляет стол.
     *
     * @param areaId Идентификатор стола.
     */
    public void delete(UUID areaId) {

        this.areaRepository.deleteById(areaId);
    }

    /**
     * Сохраняет стол.
     * Если идентификатор нулловый, то значит будет создание стола. Иначе - обновление.
     *
     * @param id Идентификатор стола.
     * @param areaId Идентификатор пространства.
     * @param number Номер стола.
     *
     * @return Сохраненное стол.
     */
    private Desk save(UUID id, UUID areaId, int number) {

        Area area = this.areaRepository.findById(areaId).orElseThrow(() ->
            new EntityNotFoundException("Area with id %s not found".formatted(areaId))
        );

        return this.deskRepository.save(Desk.builder().id(id).number(number).area(area).build());
    }
}
