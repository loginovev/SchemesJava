package ru.loginov.references.scheme.service;

import ru.loginov.references.scheme.dto.SchemeDTO;
import ru.loginov.references.scheme.dto.SchemeResponseDTO;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.util.List;
import java.util.UUID;

public interface SchemeService {

    SchemeResponseDTO getListScheme(int first, int rows, List<SortField> sortFields, List<FilterField> filters);
    SchemeDTO getScheme(UUID id, boolean needSchemeAsJSON);
    UUID newScheme(SchemeDTO schemeDTO);
    void updateScheme(SchemeDTO schemeDTO);
    void deleteScheme(UUID id);
    int count();
}
