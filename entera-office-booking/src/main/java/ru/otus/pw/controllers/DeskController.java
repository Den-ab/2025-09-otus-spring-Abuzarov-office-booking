package ru.otus.pw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pw.models.Desk;
import ru.otus.pw.services.DeskService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DeskController {

    private final DeskService deskService;

    @GetMapping(value = "/desks")
    public ResponseEntity<List<Desk>> findAllDesks() {

        final List<Desk> desks = deskService.findAll();

        return ResponseEntity.ok(desks);
    }
}
