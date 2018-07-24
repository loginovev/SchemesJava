package ru.loginov.references.scheme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchemeResponseDTO {
    private List<SchemeDTO> data = new ArrayList<>();
    private int totalRecords = 0;
}
