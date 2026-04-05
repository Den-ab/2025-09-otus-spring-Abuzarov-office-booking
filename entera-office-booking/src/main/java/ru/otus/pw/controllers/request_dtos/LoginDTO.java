package ru.otus.pw.controllers.request_dtos;

/**
 * Данные для логина пользователя.
 *
 * @param email Почта.
 * @param password Пароль.
 */
public record LoginDTO(String email, String password) {}
