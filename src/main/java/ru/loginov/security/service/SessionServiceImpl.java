package ru.loginov.security.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ru.loginov.security.dto.SessionResponseDTO;
import ru.loginov.security.exceptions.CustomDeleteYourOwnSessionException;
import ru.loginov.security.dto.SessionDTO;
import ru.loginov.security.rest.SessionRest;
import ru.loginov.utils.sort_filter.SortFilterUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Service for controlling sessions
 */
@Service
public class SessionServiceImpl implements SessionService {

    private ConcurrentMap<String,SessionDTO> sessionsMap = new ConcurrentHashMap<>();

    /**
     * The method gives a session in the sessionsMap
     * @param sessionDTO (SessionDTO)
     */
    @Override
    public void saveSession(SessionDTO sessionDTO) {
        sessionsMap.put(sessionDTO.getUserDTO().getUsername(),sessionDTO);
    }

    /**
     * The method removes a session from the sessionsMap
     * @param username  (String)
     */
    @Override
    public void deleteSessionByName(String username) {
        sessionsMap.remove(username);
    }

    /**
     * The method searches a session in the sessionsMap
     * @param username  (String)
     * @return          (SessionDTO)
     */
    @Override
    public SessionDTO findSessionByName(String username) {
        return sessionsMap.get(username);
    }

    /**
     * The method gets sessions from the sessionsMap for a lazy mode table
     * @param first         (int)               - the first session (from 0)
     * @param rows          (int)               - number of sessions
     * @param sortFields    ({@literal List<SortField>})   - the sorting fields
     * @param filters       ({@literal List<FilterField>}) - the filtering fields
     * @return              (SessionResponseDTO - sessions and total number of session in the sessionsMap
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Session,'Try to get all sessions')")
    public SessionResponseDTO getAllSessions(int first, int rows, List<SortField> sortFields, List<FilterField> filters,boolean authentication){

        SortFilterUtils.checkSortFields(new String[]{"username","firstName","surname","sessionBegin","lastActivity"},sortFields);
        SortFilterUtils.checkFilterFields(new String[]{"username","firstName","surname","sessionBegin","lastActivity"},filters);

        List<SessionDTO> response = new ArrayList<>();

        List<SessionDTO> sortedArray = new ArrayList<>();

        sessionsMap.forEach((s, sessionDTO) -> {

            boolean suitable = true;

            if (filters!=null && filters.size()!=0){

                for (FilterField filterField:filters) {

                    suitable = SortFilterUtils.suitableFilter(
                            filterField.getMatchMode(),
                            sessionDTO.getFieldByName(filterField.getFieldName()),
                            filterField.valueAsCharArray()
                    );

                    if (!suitable){
                        break;
                    }
                }
            }

            if (suitable){
                sortedArray.add(sessionDTO);
            }
        });

        if (sortedArray.size()>=first){

            if (sortFields!=null && !sortFields.isEmpty()){
                sortedArray.sort(SortFilterUtils.getSortComparator(sortFields));
            }

            for (int i=first;i<Math.min(rows,sortedArray.size());i++){
                sortedArray.get(i).authentication = authentication;
                response.add(sortedArray.get(i));
            }
        }

        return new SessionResponseDTO(response,sortedArray.size());
    }

    /**
     * The method deletes a session
     * @param username (String)
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Session,'Try to delete the session of \"'+#username+'\"')")
    public void delete(String username){

        //If user trying to delete his own session throw exception
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)){
            throw new CustomDeleteYourOwnSessionException(username);
        }

        deleteSessionByName(username);

        CustomLogManager.info(
                SessionRest.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.DELETE,
                LoggerAppStructure.Session,
                username,
                "User "+username+"'s session  was aborted"
        );
    }
}
