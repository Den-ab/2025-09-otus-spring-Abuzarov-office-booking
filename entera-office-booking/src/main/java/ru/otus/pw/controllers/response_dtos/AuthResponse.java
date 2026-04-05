package ru.otus.pw.controllers.response_dtos;

/**
 * Ответ успешного логина.
 *
 * @param token Токен.
 * @param role Роль.
 */
public record AuthResponse(String token, String role) {}
