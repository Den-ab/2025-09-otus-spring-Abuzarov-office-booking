package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.Desk;
import ru.otus.pw.repositories.DeskRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeskService {

    private final DeskRepository deskRepository;

    public List<Desk> findAll() {

        return this.deskRepository.findAll();
    }
}
