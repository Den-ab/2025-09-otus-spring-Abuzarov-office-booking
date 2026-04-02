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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с бронированиями.
 */
@RequiredArgsConstructor
@Service
public class BookingService {
    //region Fields

    /**
     * Репозиторий бронирований.
     */
    private final BookingRepository bookingRepository;

    /**
     * Репозиторий пользователей.
     */
    private final EnteraUserRepository userRepository;

    /**
     * Репозиторий столов.
     */
    private final DeskRepository deskRepository;

    /**
     * Сервис для работы с почтой.
     */
    private final MailSenderService mailSenderService;

    //endregion
    //region Public

    /**
     * Возвращает список бронирований.
     *
     * @return Список бронирований.
     */
    public List<Booking> findAll() {

        return this.bookingRepository.findAll();
    }

    /**
     * Создает бронирование.
     *
     * @param userId Идентификатор пользователя.
     * @param deskId Идентификатор стола.
     *
     * @return Созданное бронирование.
     */
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
        final Booking savedBook = this.bookingRepository.save(booking);
        this.mailSenderService.sendBookingMessageToMail(
            user.get().getEmail(),
            desk.get().getArea().getName(),
            desk.get().getNumber(),
            today.format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
        return savedBook;
    }

    /**
     * Удаляет бронирование.
     *
     * @param bookingId Идентификатор бронирования.
     */
    public void delete(UUID bookingId) {

        this.bookingRepository.deleteById(bookingId);
    }

    //endregion
}
