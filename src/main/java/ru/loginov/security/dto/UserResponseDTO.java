package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * By means of the class returns the response for the table with lazy loading (the pagination mode)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private List<UserDTO> data = new ArrayList<>();
    private int totalRecords = 0;
}
