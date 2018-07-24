package ru.loginov.security.service;

import ru.loginov.security.dto.LogColumnOptionsDTO;
import ru.loginov.security.dto.LogResponseDTO;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.io.IOException;
import java.util.List;

public interface LogService {
    LogResponseDTO getLog(int first, int rows, List<SortField> sortFields, List<FilterField> filters) throws IOException;
    LogColumnOptionsDTO getColumnOptions();
}
