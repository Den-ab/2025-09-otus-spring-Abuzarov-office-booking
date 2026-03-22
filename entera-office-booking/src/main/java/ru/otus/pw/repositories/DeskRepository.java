package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.Desk;

public interface DeskRepository extends JpaRepository<Desk, Long> { }
