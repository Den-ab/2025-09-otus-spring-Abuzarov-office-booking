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

@RequiredArgsConstructor
@Service
public class DeskService {

    private final DeskRepository deskRepository;

    private final AreaRepository areaRepository;

    public List<Desk> findAll() {

        return this.deskRepository.findAll();
    }

    public Desk create(DeskDTO deskDTO) {

        final UUID areaId = UUID.fromString(deskDTO.areaId());
        final int number = deskDTO.number();

        return this.save(null, areaId, number);
    }


    public Desk update(DeskDTO deskDTO) {

        final UUID areaId = UUID.fromString(deskDTO.areaId());
        final UUID deskId = UUID.fromString(deskDTO.id());
        final int number = deskDTO.number();

        return this.save(deskId, areaId, number);
    }

    public void delete(UUID areaId) {

        this.areaRepository.deleteById(areaId);
    }

    private Desk save(UUID id, UUID areaId, int number) {

        Area area = this.areaRepository.findById(areaId).orElseThrow(() ->
            new EntityNotFoundException("Area with id %s not found".formatted(areaId))
        );

        return this.deskRepository.save(Desk.builder().id(id).number(number).area(area).build());
    }
}
