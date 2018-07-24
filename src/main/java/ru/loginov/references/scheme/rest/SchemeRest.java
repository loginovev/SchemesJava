package ru.loginov.references.scheme.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import ru.loginov.references.scheme.dto.SchemeDTO;
import ru.loginov.references.scheme.dto.SchemeResponseDTO;
import ru.loginov.references.scheme.service.SchemeService;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Rest service for control schemes
 */
@RestController
@RequestMapping(value = "/api/scheme",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class SchemeRest {

    private SchemeService schemeService;

    @Autowired
    public void setSchemeService(SchemeService schemeService) {
        this.schemeService = schemeService;
    }

    /**
     * The method provides a lazy mode of table
     * @param first - the first row, the minimum value is 0
     * @param rows - number of returning rows
     * @param sortField - a field of sorting
     * @param sortOrder - 1 or -1
     * @param filtersEncoded encoded string of array of FilterField
     * @return {data of UserDTO[], totalRecords - number of users}
     * @throws Exception if an exception happens then a json description from 'SecurityErrorsDescription.json' file will be return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public SchemeResponseDTO getListScheme(
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

        return schemeService.getListScheme(first,rows,sortFields,filters);
    }

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public SchemeDTO getScheme(@RequestParam(name = "id") UUID schemeId) throws Exception{
        return schemeService.getScheme(schemeId,true);
    }

    @RequestMapping(value = "/new",method = RequestMethod.PUT)
    public UUID newScheme(@RequestBody SchemeDTO schemeDTO) throws Exception{
        return schemeService.newScheme(schemeDTO);
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public void updateScheme(@RequestBody SchemeDTO schemeDTO) throws Exception{
        schemeService.updateScheme(schemeDTO);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public void deleteScheme(@RequestParam(name = "id")  UUID schemeId) throws Exception{
        schemeService.deleteScheme(schemeId);
    }
}
