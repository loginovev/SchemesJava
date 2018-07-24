package ru.loginov.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.loginov.security.UserRoleEnum;

import java.util.*;

@JsonIgnoreProperties(value = {"encodedPassword"},allowSetters = true,ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOTest {

    private String username;
    private String firstName = "";
    private String surname = "";
    private String password;
    private String encodedPassword;
    private ArrayList<UserRoleEnum> authorities = new ArrayList<>();
    private SortedMap<String,Object> options = new TreeMap<>();
    private boolean banned;
}
