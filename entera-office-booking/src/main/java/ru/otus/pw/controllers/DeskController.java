package ru.otus.pw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pw.controllers.request_dtos.CreateDeskDTO;
import ru.otus.pw.models.Desk;
import ru.otus.pw.services.DeskService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class DeskController {

    private final DeskService deskService;

    @GetMapping(value = "/desks")
    public ResponseEntity<List<Desk>> findAllDesks() {

        final List<Desk> desks = this.deskService.findAll();

        return ResponseEntity.ok(desks);
    }

    @PostMapping(value = "/desks")
    public ResponseEntity<Desk> createDesk(@RequestBody CreateDeskDTO desk) {

        final UUID areaId = UUID.fromString(desk.areaId());
        final int number = desk.number();
        final Desk savedDesk = this.deskService.create(areaId, number);

        return ResponseEntity.ok(savedDesk);
    }
}
