package ru.otus.pw.controllers.request_dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Пространство.
 *
 * @param id Идентификатор.
 * @param name Наименование.
 */
public record AreaDTO(String id, @NotBlank @Size(max = 255) String name) { }
