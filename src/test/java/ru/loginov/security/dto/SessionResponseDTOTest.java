package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponseDTOTest {

    private List<SessionDTOTest> data;
    private int totalRecords;
}
