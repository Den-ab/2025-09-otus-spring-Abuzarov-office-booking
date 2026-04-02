package ru.otus.pw.controllers.request_dtos;

/**
 * Бронирование.
 *
 * @param userId Идентификатор пользователя.
 * @param deskId Идентификатор стола.
 */
public record BookDeskDTO(String userId, String deskId) { }
