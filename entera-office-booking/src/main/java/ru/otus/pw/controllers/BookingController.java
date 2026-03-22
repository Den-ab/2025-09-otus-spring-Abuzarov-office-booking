package ru.otus.pw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pw.models.Booking;
import ru.otus.pw.services.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BookingController {

    private final BookingService bookingService;

    @GetMapping(value = "/bookings")
    public ResponseEntity<List<Booking>> findAllBookings() {

        final List<Booking> bookings = bookingService.findAll();

        return ResponseEntity.ok(bookings);
    }
}
