package ru.loginov.security.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.loginov.security.dto.UserDTO;
import ru.loginov.security.dto.UserResponseDTO;
import ru.loginov.security.service.UserService;
import ru.loginov.utils.JsonUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

/**
 * Rest service for control users
 */
@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserRest {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public UserDTO getUser(@RequestParam(name = "username") String username) throws Exception{
        return userService.getUser(username);
    }

    /**
     * The method provides a lazy mode of table
     * @param first - the first row, the minimum value is 0
     * @param rows - number of returning rows
     * @param sortField - a field of sorting
     * @param sortOrder - 1 or -1
     * @param filtersEncoded encoded string of array of FilterField
     * @return {data of UserDTO[], totalRecords - number of users}
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public UserResponseDTO getAllUsers(
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

        return userService.getAllUsers(first,rows,sortFields,filters);
    }

    /**
     * the method edits a user
     * @param userDTO
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void updateUser(@RequestBody UserDTO userDTO) throws Exception{

        if(userDTO.getPassword()==null){
            userDTO.setPassword("");
        }
        userService.updateUser(userDTO);
    }

    /**
     * The method creates a new user
     * @param userDTO
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/new", method = RequestMethod.PUT)
    public void newUser(@RequestBody UserDTO userDTO) throws Exception{

        if(userDTO.getPassword()==null){
            userDTO.setPassword("");
        }
        userService.newUser(userDTO);
    }

    /**
     * The method delete the user with username of parameter
     * @param username username of deleting user
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUser(@RequestParam(name = "username") String username) throws Exception{
        userService.deleteUser(username);
    }

    /**
     * The method edits options the current user
     * @param options options of the current user
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/patchOptions", method = RequestMethod.PATCH)
    public void updateOptions(@RequestBody SortedMap<String,Object> options) throws Exception{
        userService.updateOptions(options);
    }

    /**
     * The method returns a role list
     * @return array of roles
     * @throws Exception if an exception happens then a json description from 'errorsDescription.json' file will be return
     */
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public String[] getRoles() throws Exception{
        return userService.getRoles();
    }

}
