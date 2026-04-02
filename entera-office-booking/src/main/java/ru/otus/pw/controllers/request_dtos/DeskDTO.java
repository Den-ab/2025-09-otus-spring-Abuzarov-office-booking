package ru.otus.pw.controllers.request_dtos;

/**
 * Стол.
 *
 * @param id Идентификатор пользователя.
 * @param areaId Идентификатор пространства.
 * @param number Номер стола.
 */
public record DeskDTO(String id, String areaId, int number) { }
