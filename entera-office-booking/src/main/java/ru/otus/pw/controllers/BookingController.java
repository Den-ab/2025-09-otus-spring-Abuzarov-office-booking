package ru.otus.pw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pw.controllers.request_dtos.BookDeskDTO;
import ru.otus.pw.models.Booking;
import ru.otus.pw.services.BookingService;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class BookingController {

    private final BookingService bookingService;

    @GetMapping(value = "/bookings")
    public ResponseEntity<List<Booking>> findAllBookings() {

        final List<Booking> bookings = this.bookingService.findAll();

        return ResponseEntity.ok(bookings);
    }

    @PostMapping(value = "/bookings")
    public ResponseEntity<Booking> bookDesk(@RequestBody BookDeskDTO bookDeskDTO) {

        final UUID userId = UUID.fromString(bookDeskDTO.userId());
        final UUID deskId = UUID.fromString(bookDeskDTO.deskId());

        final Booking booking = this.bookingService.bookDesk(userId, deskId);

        return ResponseEntity.ok(booking);
    }
}
