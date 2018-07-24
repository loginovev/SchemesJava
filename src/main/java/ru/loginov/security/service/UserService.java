package ru.loginov.security.service;

import ru.loginov.security.dto.UserDTO;
import ru.loginov.security.dto.UserResponseDTO;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;

import java.util.List;
import java.util.SortedMap;

public interface UserService {

    UserResponseDTO getAllUsers(int first, int rows, List<SortField> sortFields, List<FilterField> filters);
    List<UserDTO> getAllUsersWithoutPartition();
    UserDTO getUser(String username);
    void updateUser(UserDTO userDTO);
    void newUser(UserDTO userDTO) throws Exception;
    void deleteUser(String username);
    int count();
    void updateOptions(SortedMap<String,Object> options);
    String[] getRoles();
}
