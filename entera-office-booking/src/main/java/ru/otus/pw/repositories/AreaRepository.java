package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.Area;

public interface AreaRepository extends JpaRepository<Area, Long> { }
