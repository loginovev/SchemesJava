package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTOTest {

    private String username;
    private String firstName;
    private String surname;
    private Date sessionBegin;
    private Date lastActivity;
}