package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.Area;

import java.util.UUID;

public interface AreaRepository extends JpaRepository<Area, UUID> { }
