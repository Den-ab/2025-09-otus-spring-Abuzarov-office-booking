package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.controllers.request_dtos.AreaDTO;
import ru.otus.pw.models.Area;
import ru.otus.pw.repositories.AreaRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public List<Area> findAll() {

        return this.areaRepository.findAll();
    }

    public Area create(AreaDTO area) {

        return this.save(null, area.name());
    }

    public Area update(AreaDTO area) {

        final UUID areaId = UUID.fromString(area.id());

        return this.save(areaId, area.name());
    }

    public void delete(UUID areaId) {

        this.areaRepository.deleteById(areaId);
    }

    private Area save(UUID areaId, String name) {

        return this.areaRepository.save(Area.builder().id(areaId).name(name).build());
    }
}
