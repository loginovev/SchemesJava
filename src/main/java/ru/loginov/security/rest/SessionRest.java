package ru.loginov.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.loginov.security.dto.SessionResponseDTO;
import ru.loginov.security.service.SessionService;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rest service for control of sessions
 */
@RestController
@RequestMapping(value = "/api/session", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SessionRest {


    private SessionService sessionService;

    @Autowired
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * The method provides a lazy mode of table
     * Sessions will be returned as {username,firstName,surname,sessionBegin,lastActivity}(authentication==false)
     * @param first - the first row, the minimum value is 0
     * @param rows - number of returning rows
     * @param sortField - a field of sorting
     * @param sortOrder - 1 or -1
     * @param filtersEncoded encoded string of array of FilterField
     * @return {data of SessionDTO[], totalRecords - number of sessions}
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public SessionResponseDTO getAll(
            @RequestParam(name = "first") int first
            ,@RequestParam(name = "rows") int rows
            ,@RequestParam(name = "sortField",required = false) String sortField
            ,@RequestParam(name = "sortOrder",required = false,defaultValue = "0") int sortOrder
            ,@RequestParam(name = "filters",required = false) String filtersEncoded
    ) throws Exception{

        List<SortField> sortFields = new ArrayList<>();
        if (sortField!=null && !sortField.equals("")){
            sortFields.add(new SortField(sortField,sortOrder));
        }

        List<FilterField> filters = new ArrayList<>();
        if (filtersEncoded!=null && !filtersEncoded.equals("")){
            FilterField[] filterFieldsArray = JsonUtils.fromJSON(URLDecoder.decode(filtersEncoded,"UTF-8"),FilterField[].class);
            if (filterFieldsArray!=null){
                Collections.addAll(filters,filterFieldsArray);
            }
        }

        return sessionService.getAllSessions(first,rows,sortFields,filters,false);
    }

    /**
     * The method aborted the 'username' session
     * @param username user
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteSession(@RequestParam(name = "username") String username) throws Exception{
        sessionService.delete(username);
    }
}
