package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogDTOTest {

    private String date;
    private String messageType;
    private String username;
    private String event;
    private String structure;
    private String data;
    private String message;
}
