package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.EnteraUser;

import java.util.UUID;

public interface EnteraUserRepository extends JpaRepository<EnteraUser, UUID> { }
