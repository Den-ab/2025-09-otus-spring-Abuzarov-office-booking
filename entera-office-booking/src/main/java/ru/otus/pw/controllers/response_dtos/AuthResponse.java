package ru.otus.pw.controllers.response_dtos;

/**
 * Ответ успешного логина.
 *
 * @param token Токен.
 */
public record AuthResponse(String token) {}
