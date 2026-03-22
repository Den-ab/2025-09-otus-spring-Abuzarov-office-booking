package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.Area;
import ru.otus.pw.repositories.AreaRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public List<Area> findAll() {

        return this.areaRepository.findAll();
    }

    public Area create(Area area) {

        return this.areaRepository.save(area);
    }
}
