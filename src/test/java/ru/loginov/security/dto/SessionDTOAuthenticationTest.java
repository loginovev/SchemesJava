package ru.loginov.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionDTOAuthenticationTest {

    private String username;
    private String securityToken;
    private ArrayList<String> authorities = new ArrayList<>();
    private SortedMap<String,Object> options = new TreeMap<>();
}
