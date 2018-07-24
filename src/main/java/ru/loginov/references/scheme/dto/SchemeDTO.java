package ru.loginov.references.scheme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchemeDTO {
    private UUID id;
    private String title;
    private String schemeAsJSON;
}
