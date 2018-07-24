package ru.loginov.security.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.codec.digest.DigestUtils;
import ru.loginov.security.UserRoleEnum;
import ru.loginov.utils.logging.CustomLogManager;
import ru.loginov.utils.logging.LoggerAppStructure;
import ru.loginov.utils.logging.LoggerEvents;
import ru.loginov.utils.sort_filter.sort.Sorted;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Record in json is made as toJson. If authentication=false, then in json writes information for an output in the table, otherwise, for authorization.
 * Implements the ru.loginov.utils.sort_filter.sort.Sorted interface for a possibility of sorting in ru.loginov.utils.sort_filter.SortFilterUtils.
 *
 */
public class SessionDTO implements Sorted {

    public Boolean authentication=false;

    private UserDTO userDTO = null;
    private String securityToken;
    private Date sessionBegin;
    private Date lastActivity;

    public SessionDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
        this.securityToken = DigestUtils.sha256Hex(userDTO.getUsername()+":"+userDTO.getEncodedPassword()+":"+ Calendar.getInstance().getTime().toString());
        this.sessionBegin = Calendar.getInstance().getTime();
        this.lastActivity = sessionBegin;
    }

    public SessionDTO() {
    }

    @JsonRawValue
    @JsonValue
    public String toJson(){

        if (authentication){

            Writer writer = new StringWriter();
            JsonFactory jsonFactory = new JsonFactory();
            try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer)){

                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("username",userDTO.getUsername());
                jsonGenerator.writeStringField("securityToken",securityToken);

                jsonGenerator.writeFieldName("authorities");
                jsonGenerator.writeStartArray();
                for (UserRoleEnum userRoleEnum :userDTO.getAuthorities()) {
                    jsonGenerator.writeString(userRoleEnum.name());
                }
                jsonGenerator.writeEndArray();

                jsonGenerator.writeFieldName("options");
                jsonGenerator.writeStartObject();
                for (Map.Entry<String,Object> option:userDTO.getOptions().entrySet()) {
                    jsonGenerator.writeStringField(option.getKey(),option.getValue().toString());
                }
                jsonGenerator.writeEndObject();

                jsonGenerator.writeEndObject();
            }catch (IOException ex){
                CustomLogManager.error(
                        SessionDTO.class.getName(),
                        userDTO.getUsername(),
                        LoggerEvents.LOGIN,
                        LoggerAppStructure.Logging,
                        "",
                        ex.getMessage()
                );
            }
            return writer.toString();
        }else {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

            Writer writer = new StringWriter();
            JsonFactory jsonFactory = new JsonFactory();
            try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer)){

                jsonGenerator.writeStartObject();

                jsonGenerator.writeStringField("username",userDTO.getUsername());
                jsonGenerator.writeStringField("firstName",userDTO.getFirstName());
                jsonGenerator.writeStringField("surname",userDTO.getSurname());

                jsonGenerator.writeStringField("sessionBegin",df.format(sessionBegin));
                jsonGenerator.writeStringField("lastActivity",df.format(lastActivity));

                jsonGenerator.writeEndObject();
            }catch (IOException ex){
                CustomLogManager.error(
                        SessionDTO.class.getName(),
                        userDTO.getUsername(),
                        LoggerEvents.REQUEST,
                        LoggerAppStructure.Session,
                        "",
                        ex.getMessage()
                );
            }
            return writer.toString();
        }
    }

    @Override
    public char[] getFieldByName(String fieldName){
        switch (fieldName){
            case "username":{
                return userDTO.getUsername().toCharArray();
            }
            case "firstName":{
                return userDTO.getFirstName().toCharArray();
            }
            case "surname":{
                return userDTO.getSurname().toCharArray();
            }
            case "sessionBegin":{
                return getSessionBegin().toString().toCharArray();
            }
            case "lastActivity":{
                return getLastActivity().toString().toCharArray();
            }
        }
        return new char[0];
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public String getSecurityToken() {
        return securityToken;
    }

    public Date getSessionBegin() {
        return sessionBegin;
    }

    public Date getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(Date lastActivity) {
        this.lastActivity = lastActivity;
    }
}
