package ru.loginov.security.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.loginov.security.UserRoleEnum;
import ru.loginov.security.dto.UserDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 *
 */

@Entity(name = "security_entity_UserData")
@Table(indexes = {
        @Index(name = "ndx_UserData",columnList = "firstName,surname,banned")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserData {

    @Id
    private String username;

    private String firstName = "";
    private String surname = "";

    @NotNull
    private String password;
    @NotNull
    private String encodedPassword;
    private boolean banned = false;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "security_entity_UserData_authorities")
    private List<UserRoleData> authorities = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "security_entity_UserData_options")
    @MapKeyJoinColumn(name = "option_username", referencedColumnName = "username")
    private Map<String,UserOptionsData> options = new HashMap<>();

    public UserData(UserDTO userDTO, BCryptPasswordEncoder passwordEncoder){

        this.username = userDTO.getUsername();
        this.firstName = userDTO.getFirstName();
        this.surname = userDTO.getSurname();
        this.password = userDTO.getPassword();
        this.encodedPassword = passwordEncoder.encode(this.password);


        this.banned = userDTO.isBanned();

        this.authorities.clear();
        for (UserRoleEnum userRoleEnum:userDTO.getAuthorities()) {
            this.authorities.add(new UserRoleData(userRoleEnum));
        }

        this.options.clear();
        for (Map.Entry<String,Object> option:userDTO.getOptions().entrySet()) {
            this.options.put(option.getKey(),new UserOptionsData(option.getValue()));
        }
    }

    public void update(UserDTO userDTO, BCryptPasswordEncoder passwordEncoder){

        this.firstName = userDTO.getFirstName();
        this.surname = userDTO.getSurname();

        if (userDTO.getPassword()!=null && !userDTO.getPassword().equals("")){
            this.password = userDTO.getPassword();
            this.encodedPassword = passwordEncoder.encode(this.password);
        }

        this.banned = userDTO.isBanned();

        this.authorities.clear();
        for (UserRoleEnum userRoleEnum:userDTO.getAuthorities()) {
            this.authorities.add(new UserRoleData(userRoleEnum));
        }

        this.options.clear();
        for (Map.Entry<String,Object> option:userDTO.getOptions().entrySet()) {
            this.options.put(option.getKey(),new UserOptionsData(option.getValue()));
        }
    }

    public void patchOptions(SortedMap<String, Object> options){

        this.options.clear();
        for (Map.Entry<String,Object> option:options.entrySet()) {
            this.options.put(option.getKey(),new UserOptionsData(option.getValue()));
        }
    }

    public UserDTO getUserDTO(){

        UserDTO userDTO = new UserDTO(
                getUsername(),
                getFirstName(),
                getSurname(),
                getPassword(),
                getEncodedPassword(),
                isBanned(),
                new ArrayList<>()
        );

        for (UserRoleData userRoleData:getAuthorities()) {
            userDTO.getAuthorities().add(userRoleData.getRole());
        }

        userDTO.getOptions().clear();
        for (Map.Entry<String,UserOptionsData> userOptionsDataEntry:getOptions().entrySet()) {
            userDTO.getOptions().put(userOptionsDataEntry.getKey(),(Object) userOptionsDataEntry.getValue().getValue());
        }

        return userDTO;
    }
}
