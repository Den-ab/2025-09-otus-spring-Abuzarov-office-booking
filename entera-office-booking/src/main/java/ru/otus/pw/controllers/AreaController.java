package ru.otus.pw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pw.controllers.request_dtos.AreaDTO;
import ru.otus.pw.models.Area;
import ru.otus.pw.services.AreaService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class AreaController {

    private final AreaService areaService;

    @GetMapping(value = "/areas")
    public ResponseEntity<List<Area>> findAllAreas() {

        final List<Area> areas = this.areaService.findAll();

        return ResponseEntity.ok(areas);
    }

    @PostMapping(value = "/areas")
    public ResponseEntity<Area> createArea(@RequestBody AreaDTO area) {

        final Area savedArea = this.areaService.create(area);

        return ResponseEntity.ok(savedArea);
    }

    @PutMapping(value = "/areas")
    public ResponseEntity<Area> updateArea(@RequestBody AreaDTO area) {

        final Area savedArea = this.areaService.update(area);

        return ResponseEntity.ok(savedArea);
    }

    @DeleteMapping(value = "/areas/{id}")
    public void deleteArea(@PathVariable String id) {

        this.areaService.delete(UUID.fromString(id));
    }
}
