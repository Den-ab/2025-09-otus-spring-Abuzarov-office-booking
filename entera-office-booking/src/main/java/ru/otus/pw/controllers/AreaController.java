package ru.otus.pw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pw.models.Area;
import ru.otus.pw.services.AreaService;

import java.util.List;

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
    public ResponseEntity<Area> createArea(@RequestBody Area area) {

        final Area savedArea = this.areaService.create(area);

        return ResponseEntity.ok(savedArea);
    }
}
