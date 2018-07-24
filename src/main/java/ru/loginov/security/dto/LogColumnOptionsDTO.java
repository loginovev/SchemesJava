package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The class serves for returns of values of columns of the client logging system
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogColumnOptionsDTO {

    private String[] messageType;
    private String[] username;
    private String[] event;
    private String[] structure;
}
