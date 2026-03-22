package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.EnteraUser;

public interface EnteraUserRepository extends JpaRepository<EnteraUser, Long> { }
