package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.Desk;

import java.util.UUID;

/**
 * Репозиторий столов.
 */
public interface DeskRepository extends JpaRepository<Desk, UUID> { }
