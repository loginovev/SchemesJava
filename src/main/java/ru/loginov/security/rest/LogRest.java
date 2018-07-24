package ru.loginov.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.loginov.security.dto.LogColumnOptionsDTO;
import ru.loginov.security.dto.LogResponseDTO;
import ru.loginov.security.service.LogService;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.net.URLDecoder;
import java.util.*;

/**
 * Rest service for receiving a log
 */
@RestController
@RequestMapping(value = "/api/log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class LogRest {

    private LogService logService;

    @Autowired
    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    /**
     * The method provides a lazy mode of table
     * @param first - the first row, the minimum value is 0
     * @param rows - number of returning rows
     * @param sortField - a field of sorting
     * @param sortOrder - 1 or -1
     * @param filtersEncoded encoded string of array of FilterField
     * @return {data of LogDTO[], totalRecords - number rows in log}
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/records", method = RequestMethod.GET)
    public LogResponseDTO getAll(
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

        return logService.getLog(first,rows,sortFields,filters);
    }

    /**
     * The method provides filling of options of a lazy table columns
     * @return columns: {messageType,username,event,structure}
     */
    @RequestMapping(value = "/columnOptions", method = RequestMethod.GET)
    public LogColumnOptionsDTO getColumnOptions(){
        return logService.getColumnOptions();
    }

}
