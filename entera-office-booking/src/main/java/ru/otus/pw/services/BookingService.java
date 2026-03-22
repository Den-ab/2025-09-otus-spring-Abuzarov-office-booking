package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.Booking;
import ru.otus.pw.repositories.BookingRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public List<Booking> findAll() {

        return this.bookingRepository.findAll();
    }
}
