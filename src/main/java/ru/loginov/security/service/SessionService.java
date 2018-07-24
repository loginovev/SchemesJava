package ru.loginov.security.service;

import ru.loginov.security.dto.SessionDTO;
import ru.loginov.security.dto.SessionResponseDTO;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.util.List;

public interface SessionService {
    SessionDTO findSessionByName(String username);
    SessionResponseDTO getAllSessions(int first, int rows, List<SortField> sortFields, List<FilterField> filters,boolean authentication);
    void delete(String username) throws Exception;
    void deleteSessionByName(String username);
    void saveSession(SessionDTO sessionDTO);
}
