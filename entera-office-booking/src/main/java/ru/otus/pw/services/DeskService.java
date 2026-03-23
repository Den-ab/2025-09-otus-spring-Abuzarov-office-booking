package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.Area;
import ru.otus.pw.models.Desk;
import ru.otus.pw.repositories.AreaRepository;
import ru.otus.pw.repositories.DeskRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeskService {

    private final DeskRepository deskRepository;

    private final AreaRepository areaRepository;

    public List<Desk> findAll() {

        return this.deskRepository.findAll();
    }

    public Desk create(UUID areaId, int number) {

        final Optional<Area> area = this.areaRepository.findById(areaId);

        if (area.isEmpty()) {

            throw new IllegalStateException("Area doesn't exist");
        }

        final Desk desk = Desk.builder().area(area.get()).number(number).build();

        return this.deskRepository.save(desk);
    }
}
