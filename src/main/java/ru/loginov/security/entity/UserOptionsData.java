package ru.loginov.security.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserOptionsData {

    private String optionString;
    private Boolean optionBoolean;
    private Integer optionInteger;

    public UserOptionsData(Object optionValue) {

        switch (optionValue.getClass().getName()){
            case "java.lang.String":{
                optionString = (String) optionValue;
                break;
            }
            case "java.lang.Boolean":{
                optionBoolean = (Boolean) optionValue;
                break;
            }
            case "java.lang.Integer":{
                optionInteger = (Integer) optionValue;
                break;
            }
        }
    }

    public Object getValue(){
        if (optionString!=null){
            return optionString;
        }
        if (optionBoolean!=null){
            return optionBoolean;
        }
        if (optionInteger!=null){
            return optionInteger;
        }

        return null;
    }
}
