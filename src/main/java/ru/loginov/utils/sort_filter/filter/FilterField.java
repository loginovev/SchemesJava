package ru.loginov.utils.sort_filter.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.loginov.security.exceptions.CustomWrongFilterFieldsException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FilterField {

    private String fieldName;
    private FilterMatchMode matchMode;
    private Object value;

    public String queryExpressionForField(String prefix, Class<?> dataClass){
        String queryString = "";

        String typeField = null;

        for (Field field:dataClass.getDeclaredFields()) {
            if (field.getName().equals(getFieldName())){
                typeField = filedType(field.getType());
                break;
            }
        }
        if (typeField==null){
            throw new CustomWrongFilterFieldsException();
        }

        if (getMatchMode() == FilterMatchMode.in){

            if (getValue() instanceof ArrayList){

                try {
                    switch (typeField){
                        case "Integer":{
                            for (Integer valueInteger:(List<Integer>) getValue()) {
                                queryString = queryString + (queryString.equals("") ? "" : ",") + valueInteger.toString();
                            }
                            break;
                        }
                        case "Double":{
                            for (Double valueInteger:(List<Double>) getValue()) {
                                queryString = queryString + (queryString.equals("") ? "" : ",") + valueInteger.toString();
                            }
                            break;
                        }
                        case "String":{
                            for (String valueInteger:(List<String>) getValue()) {
                                queryString = queryString + (queryString.equals("") ? "" : ",") + "'" + valueInteger + "'";
                            }
                            break;
                        }
                        case "Date":{
                            for (String valueInteger:(List<String>) getValue()) {
                                queryString = queryString + (queryString.equals("") ? "" : ",") + "'" + valueInteger + "'";
                            }
                            break;
                        }
                    }
                }catch (Exception e){
                    throw new CustomWrongFilterFieldsException(e.getMessage());
                }

                queryString = prefix + "." + getFieldName() + " in(" + queryString+")";

            }else{
                throw new CustomWrongFilterFieldsException();
            }

        }else{

            boolean needQuarters = false;
            switch (typeField){
                case "Integer":{
                    queryString = ((Integer) getValue()).toString();
                    break;
                }
                case "Double":{
                    queryString = ((Double) getValue()).toString();
                    break;
                }
                case "String":{
                    queryString = (String) getValue();
                    needQuarters = true;
                    break;
                }
                case "Boolean":{
                    queryString = ((Boolean) getValue()).toString();
                    break;
                }
                case "Date":{
                    queryString = (String) value;
                    //queryString = LocalDateTime.parse((String) value).format(DateTimeFormatter.ISO_DATE_TIME);
                    needQuarters = true;
                    break;
                }
            }

            switch (getMatchMode()){
                case equals:{
                    queryString = prefix + "." + getFieldName() + "=" + (needQuarters ? "'" : "") + queryString + (needQuarters ? "'" : "") + " ";
                    break;
                }
                case startWith:{
                    queryString = prefix + "." + getFieldName() + " like '" + queryString + "%' ";
                    break;
                }
                case endWith:{
                    queryString = prefix + "." + getFieldName() + " like '%" + queryString + "' ";
                    break;
                }
                case contains:{
                    queryString = prefix + "." + getFieldName() + " like '%" + queryString + "%' ";
                    break;
                }
            }
        }

        return queryString;
    }

    private String filedType(Class<?> type){

        String response;

        switch (type.getName()){
            case "int":
            case "byte":
            case "long":{
                response = "Integer";
                break;
            }
            case "float":
            case "double":{
                response = "Double";
                break;
            }
            case "java.lang.String":{
                response = "String";
                break;
            }
            case "java.util.Date":
            case "java.sql.Date":
                {
                response = "Date";
                break;
            }
            case "java.lang.Boolean":
            case "boolean":
                {
                response = "Boolean";
                break;
            }
            default:{
                response = "String";
                break;
            }
        }

        return response;
    }

    public char[] valueAsCharArray(){
        if(value instanceof char[]){
            return (char[]) value;
        }else {
            return String.valueOf(value).toCharArray();
        }
    }
}