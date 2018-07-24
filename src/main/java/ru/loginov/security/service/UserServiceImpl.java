package ru.loginov.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.loginov.security.UserRoleEnum;
import ru.loginov.security.dto.UserDTO;
import ru.loginov.security.dto.UserResponseDTO;
import ru.loginov.security.entity.UserData;
import ru.loginov.security.exceptions.CustomUserHasAlreadyRegisteredException;
import ru.loginov.security.exceptions.CustomUsernameNotFoundException;
import ru.loginov.security.rest.UserRest;
import ru.loginov.utils.sort_filter.SortFilterUtils;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.sort.SortField;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

/**
 * Users service
 */
@Service
public class UserServiceImpl implements UserService {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The method gets users for a lazy mode table
     * @param first         (int)                          - the first user (from 0)
     * @param rows          (int)                          - amount of users
     * @param sortFields    ({@literal List<SortField>})   - sorting fields
     * @param filters       ({@literal List<FilterField>}) - filtering fields
     * @return              (UserResponseDTO)              - users and total amount of users
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Reference_Users,'Try to get all the users')")
    public UserResponseDTO getAllUsers(int first, int rows, List<SortField> sortFields, List<FilterField> filters) {

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        String queryString = "select u from ru.loginov.security.entity.UserData u ";

        if (filters.size()>0){
            queryString = queryString+"\n" + "where "+ SortFilterUtils.getQueryFilterExpression(filters,"u",UserData.class);
        }

        if (sortFields.size()>0){
            queryString = queryString+"\n" + "order by " + SortFilterUtils.getQuerySortExpression(sortFields,"u");
        }

        List<UserData> userDataList = entityManager.createQuery(queryString,UserData.class)
                .setFirstResult(first)
                .setMaxResults(rows)
                .getResultList();

        for (UserData userData:userDataList) {
            userResponseDTO.getData().add(userData.getUserDTO());
        }

        userResponseDTO.setTotalRecords(count());

        return userResponseDTO;
    }

    /**
     * The method gets users for a normal mode table
     * @return {@literal List<UserDTO>}
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Reference_Users,'Try to get all the users')")
    public List<UserDTO> getAllUsersWithoutPartition() {

        List<UserDTO> userDTOList = new ArrayList<>();

        List<UserData> userDataList = entityManager.createQuery("select u from ru.loginov.security.entity.UserData u",UserData.class).getResultList();

        for (UserData userData:userDataList) {
            userDTOList.add(userData.getUserDTO());
        }

        return userDTOList;
    }

    /**
     * The method gets an user by username
     * @param username (String)
     * @return (UserDTO)
     */
    @Override
    public UserDTO getUser(String username) {

        UserData userData = entityManager.find(UserData.class,username);
        if (userData!=null){
            return userData.getUserDTO();
        }else{
            throw new CustomUsernameNotFoundException(UserServiceImpl.class.getName(),username);
        }
    }

    /**
     * The method edits an user
     * @param userDTO (UserDTO)
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Reference_Users,'Try to edit user '+#userDTO.username)")
    @Transactional
    public void updateUser(UserDTO userDTO) {

        UserData userData = entityManager.find(UserData.class,userDTO.getUsername());
        if (userData!=null){
            userData.update(userDTO,passwordEncoder);
            entityManager.persist(userData);
        }else{
            throw new CustomUsernameNotFoundException(UserServiceImpl.class.getName(),userDTO.getUsername());
        }

        CustomLogManager.info(
                UserRest.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.EDIT,
                LoggerAppStructure.Reference_Users,
                userDTO.getUsername(),
                "User '"+userDTO.getUsername()+"' was edited"
        );
    }

    /**
     * The method creates an user
     * @param userDTO (UserDTO)
     * @throws Exception
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Reference_Users,'Try to create an user with username '+#userDTO.username)")
    @Transactional
    public void newUser(UserDTO userDTO) throws Exception {

        if (entityManager.find(UserData.class,userDTO.getUsername())==null){
            entityManager.persist(new UserData(userDTO,passwordEncoder));
        }else{
            throw new CustomUserHasAlreadyRegisteredException(userDTO.getUsername());
        }

        CustomLogManager.info(
                UserRest.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.NEW,
                LoggerAppStructure.Reference_Users,
                userDTO.getUsername(),
                "User '"+userDTO.getUsername()+"' was created"
        );
    }

    /**
     * The method deletes an user
     * @param username (String)
     */
    @Override
    @PreAuthorize("@Security.hasPermissionWithLogging(principal,T(ru.loginov.security.UserRoleEnum).ADMIN,T(ru.loginov.utils.logging.LoggerAppStructure).Reference_Users,'Try to delete the user with username \"'+#username+'\"')")
    @Transactional
    public void deleteUser(String username) {

        UserData userData = entityManager.find(UserData.class,username);
        if (userData!=null){
            entityManager.remove(userData);
        }else{
            throw new CustomUsernameNotFoundException(UserServiceImpl.class.getName(),username);
        }

        CustomLogManager.info(
                UserRest.class.getName(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                LoggerEvents.DELETE,
                LoggerAppStructure.Reference_Users,
                username,
                "User '"+username+"' was deleted"
        );
    }

    /**
     * The method gets the total amount of users
     * @return (int)
     */
    @Override
    public int count() {
        return entityManager.createQuery("select count(*) from ru.loginov.security.entity.UserData",Long.class)
                .getSingleResult()
                .intValue();
    }

    /**
     * The method edits the options of an current user
     * @param options ({@literal SortedMap<String, Object>})
     */
    @Override
    @Transactional
    public void updateOptions(SortedMap<String, Object> options) {

        UserData userData = entityManager.find(UserData.class,SecurityContextHolder.getContext().getAuthentication().getName());
        if (userData!=null){
            userData.patchOptions(options);
            entityManager.persist(userData);
        }else{
            throw new CustomUsernameNotFoundException(UserServiceImpl.class.getName(),SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }

    /**
     * The method gets the array of roles from UserRoleEnum
     * @return (String[])
     */
    @Override
    public String[] getRoles() {
        String[] response = new String[UserRoleEnum.values().length];
        int i = 0;
        for (UserRoleEnum roleEnum:UserRoleEnum.values()) {
            response[i++] = roleEnum.toString();
        }
        Arrays.sort(response);
        return response;
    }
}
