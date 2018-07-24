package ru.loginov.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.loginov.security.UserRoleEnum;
import ru.loginov.utils.sort_filter.sort.Sorted;

import java.util.*;

/**
 * In case of serialization of 'password' and 'encodedPassword' fields in json do not write.
 * When deserialization this fields from json is read.
 * Implements the ru.loginov.utils.sort_filter.sort.Sorted interface for a possibility of sorting in ru.loginov.utils.sort_filter.SortFilterUtils.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(value = {"password","encodedPassword"},allowSetters = true,ignoreUnknown = true)
public class UserDTO implements Sorted {

    private String username;
    private String firstName = "";
    private String surname = "";
    private String password;
    private String encodedPassword;
    private boolean banned = false;
    private List<UserRoleEnum> authorities = new ArrayList<>();
    private SortedMap<String,Object> options = new TreeMap<>();

    public UserDTO(
            String username
            ,String firstName
            ,String surname
            ,String password
            ,String encodedPassword
            ,boolean banned
            ,ArrayList<UserRoleEnum> authorities
    ) {
        this.username = username;
        this.firstName = firstName;
        this.surname = surname;
        this.password = password;
        this.encodedPassword = encodedPassword;
        this.banned = banned;
        this.authorities = authorities;

        this.options.put("theme","omega");
        this.options.put("language","ru");
    }

    @Override
    public char[] getFieldByName(String fieldName){
        switch (fieldName){
            case "username":{
                return getUsername().toCharArray();
            }
            case "firstName":{
                return getFirstName().toCharArray();
            }
            case "surname":{
                return getSurname().toCharArray();
            }
        }
        return new char[0];
    }

    public UserDTO setAuthoritiesByUserRoleEnum(UserRoleEnum ... authority) {
        Collections.addAll(this.authorities, authority);
        return this;
    }
}
