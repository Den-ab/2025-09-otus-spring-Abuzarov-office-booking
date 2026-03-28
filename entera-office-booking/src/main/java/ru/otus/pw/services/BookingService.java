package ru.otus.pw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.pw.models.Booking;
import ru.otus.pw.models.Desk;
import ru.otus.pw.models.EnteraUser;
import ru.otus.pw.repositories.BookingRepository;
import ru.otus.pw.repositories.DeskRepository;
import ru.otus.pw.repositories.EnteraUserRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    private final EnteraUserRepository userRepository;

    private final DeskRepository deskRepository;

    public List<Booking> findAll() {

        return this.bookingRepository.findAll();
    }

    public Booking bookDesk(UUID userId, UUID deskId) {

        final Instant now = Instant.now();
        final ZoneId zoneId = ZoneId.systemDefault();
        final LocalDate today = now.atZone(zoneId).toLocalDate();
        final List<Booking> bookings = this.bookingRepository.findByUserIdAndDeskId(userId, deskId);

        if (bookings.stream().anyMatch(booking -> booking.getDate().atZone(zoneId).toLocalDate().equals(today))) {

            throw new IllegalStateException("You have already booked this desk for today.");
        }

        final Optional<EnteraUser> user = this.userRepository.findById(userId);
        final Optional<Desk> desk = this.deskRepository.findById(deskId);

        if (user.isEmpty() || desk.isEmpty()) {

            throw new IllegalStateException("User and/or desk don't exist.");
        }

        final Booking booking = Booking.builder().desk(desk.get()).user(user.get()).date(now).build();

        return this.bookingRepository.save(booking);
    }

    public void delete(UUID bookingId) {

        this.bookingRepository.deleteById(bookingId);
    }
}
