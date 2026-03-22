package ru.otus.pw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.pw.models.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> { }
