package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogResponseDTOTest {

    private LogDTOTest[] data;
    private int totalRecords;
}
