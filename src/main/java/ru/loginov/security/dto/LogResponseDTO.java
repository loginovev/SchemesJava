package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * By means of the class returns the response for the table with lazy loading (the pagination mode)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponseDTO {

    private LogDTO[] data = new LogDTO[0];
    private int totalRecords = 0;
}
