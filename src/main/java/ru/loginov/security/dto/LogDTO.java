package ru.loginov.security.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.loginov.utils.sort_filter.sort.Sorted;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * the class uses the char type for increase in speed of operation.
 * Record in json is made as toJson.
 * Implements the ru.loginov.utils.sort_filter.sort.Sorted interface for a possibility of sorting in ru.loginov.utils.sort_filter.SortFilterUtils.
 */
@Getter
@NoArgsConstructor
public class LogDTO implements Sorted {

    private char[] date;
    private char[] messageType;
    private char[] username;
    private char[] event;
    private char[] structure;
    private char[] data;
    private char[] message;

    public LogDTO(char[][] fields, int[] fieldsLength){

        this.date = new char[fieldsLength[0]];
        System.arraycopy(fields[0],0,this.date,0,fieldsLength[0]);

        this.messageType = new char[fieldsLength[1]];
        System.arraycopy(fields[1],0,this.messageType,0,fieldsLength[1]);

        this.username = new char[fieldsLength[4]];
        System.arraycopy(fields[4],0,this.username,0,fieldsLength[4]);

        this.event = new char[fieldsLength[5]];
        System.arraycopy(fields[5],0,this.event,0,fieldsLength[5]);

        this.structure = new char[fieldsLength[6]];
        System.arraycopy(fields[6],0,this.structure,0,fieldsLength[6]);

        this.data = new char[fieldsLength[7]];
        System.arraycopy(fields[7],0,this.data,0,fieldsLength[7]);

        this.message = new char[fieldsLength[8]];
        System.arraycopy(fields[8],0,this.message,0,fieldsLength[8]);
    }

    @JsonRawValue
    @JsonValue
    public String toJson(){

        Writer writer = new StringWriter();
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(writer)){

            jsonGenerator.writeStartObject();


            jsonGenerator.writeFieldName("date");
            jsonGenerator.writeString(date,0,date.length);

            jsonGenerator.writeFieldName("messageType");
            jsonGenerator.writeString(messageType,0,messageType.length);

            jsonGenerator.writeFieldName("username");
            jsonGenerator.writeString(username,0,username.length);

            jsonGenerator.writeFieldName("event");
            jsonGenerator.writeString(event,0,event.length);

            jsonGenerator.writeFieldName("structure");
            jsonGenerator.writeString(structure,0,structure.length);

            jsonGenerator.writeFieldName("data");
            jsonGenerator.writeString(data,0,data.length);

            jsonGenerator.writeFieldName("message");
            jsonGenerator.writeString(message,0,message.length);

            jsonGenerator.writeEndObject();
        }catch (IOException ex){
            System.out.println(ex.getMessage());
        }
        return writer.toString();
    }

    public char[] getFieldByName(String fieldName){
        switch (fieldName){
            case "date":{
                return date;
            }
            case "messageType":{
                return messageType;
            }
            case "username":{
                return username;
            }
            case "event":{
                return event;
            }
            case "structure":{
                return structure;
            }
            case "data":{
                return data;
            }
            case "message":{
                return message;
            }
        }
        return new char[0];
    }
}
