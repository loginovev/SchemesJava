package ru.loginov.utils.sort_filter;

import ru.loginov.security.exceptions.CustomWrongFilterFieldsException;
import ru.loginov.security.exceptions.CustomWrongSortFieldsException;
import ru.loginov.utils.sort_filter.filter.FilterField;
import ru.loginov.utils.sort_filter.filter.FilterMatchMode;
import ru.loginov.utils.sort_filter.sort.SortComparator;
import ru.loginov.utils.sort_filter.sort.SortField;
import ru.loginov.utils.sort_filter.sort.Sorted;

import java.util.Comparator;
import java.util.List;

/**
 * The class for sorting anf filtering
 */
public class SortFilterUtils {

    public static boolean suitableFilter(FilterMatchMode type, char[] value1, char[] value2){
        return suitableFilter(type,value1,value1.length,value2);
    }

    public static boolean suitableFilter(FilterMatchMode type, char[] value1,int lengthValue1, char[] value2){

        switch (type.name()){
            case "startWith":{
                if (lengthValue1<value2.length){
                    return false;
                }
                for(int suitableCount=0;suitableCount<value2.length;suitableCount++){
                    if (value1[suitableCount]!=value2[suitableCount]){
                        return false;
                    }
                }
                break;
            }
            case "contains":{
                if (lengthValue1<value2.length){
                    return false;
                }
                boolean suitable = false;

                for(int suitableCount=0;suitableCount<lengthValue1-value2.length;suitableCount++){
                    if (value1[suitableCount]==value2[0]){
                        suitable = true;
                        for (int j=0;j<value2.length;j++){
                            if (value1[suitableCount+j]!=value2[j]){
                                suitable = false;
                                break;
                            }
                        }
                    }
                }
                if (!suitable){
                    return false;
                }
                break;
            }
            case "endWith":{
                if (lengthValue1<value2.length){
                    return false;
                }
                for(int suitableCount=value2.length-1;suitableCount>=0;suitableCount--){
                    if (value1[lengthValue1 - suitableCount - 1]!=value2[suitableCount]){
                        return false;
                    }
                }
                break;
            }
            case "equals":{
                if (lengthValue1!=value2.length){
                    return false;
                }
                for(int suitableCount=0;suitableCount<value2.length;suitableCount++){
                    if (value1[suitableCount]!=value2[suitableCount]){
                        return false;
                    }
                }
                break;
            }
        }

        return true;
    }

    public static Comparator<Sorted> getSortComparator(List<SortField> sortFields){

        Comparator<Sorted> response = null;

        for (SortField sortField:sortFields) {
            if (response==null){
                if (sortField.getSortOrder()==1){
                    response = new SortComparator(sortField.getSortField());
                }else{
                    response = new SortComparator(sortField.getSortField()).reversed();
                }

            }else {
                if (sortField.getSortOrder()==1){
                    response = response.thenComparing(new SortComparator(sortField.getSortField()));
                }else{
                    response = response.thenComparing(new SortComparator(sortField.getSortField()).reversed());
                }

            }
        }

        return response;
    }

    public static String getQueryFilterExpression(List<FilterField> filters, String prefix, Class<?> dataClass){

        String queryString = "";

        for (FilterField filterField:filters) {
            queryString = queryString + (queryString.equals("") ? "" : " and ") + filterField.queryExpressionForField(prefix,dataClass);
        }

        return queryString;
    }

    public static String getQuerySortExpression(List<SortField> sortFields, String prefix){

        String queryString = "";

        for (SortField sortField:sortFields) {
            queryString = queryString + (queryString.equals("") ? "" : ", ") + prefix + "." + sortField.getSortField() + (sortField.getSortOrder()==1 ? " asc " : " desc ");
        }

        return queryString;
    }

    public static void checkFilterFields(String[] fields, List<FilterField> filterFields) throws CustomWrongFilterFieldsException {

        boolean fieldSuitable = false;
        for (FilterField filterField:filterFields) {

            if (
                    filterField.getMatchMode()!=FilterMatchMode.equals &&
                            filterField.getMatchMode()!=FilterMatchMode.contains &&
                            filterField.getMatchMode()!=FilterMatchMode.endWith &&
                            filterField.getMatchMode()!=FilterMatchMode.startWith &&
                            filterField.getMatchMode()!=FilterMatchMode.in
                    ){
                throw new CustomWrongFilterFieldsException();
            }

            for (String s:fields) {
                if (filterField.getFieldName().equals(s)){
                    fieldSuitable = true;
                    break;
                }
            }
            if (!fieldSuitable){
                throw new CustomWrongFilterFieldsException();
            }
        }
    }

    public static void checkSortFields(String[] fields, List<SortField> sortFields) throws CustomWrongSortFieldsException {

        boolean fieldSuitable = false;
        for (SortField sortField:sortFields) {

            if (sortField.getSortOrder()!=1 && sortField.getSortOrder()!=-1){
                throw new CustomWrongSortFieldsException();
            }

            for (String s:fields) {
                if (sortField.getSortField().equals(s)){
                    fieldSuitable = true;
                    break;
                }
            }
            if (!fieldSuitable){
                throw new CustomWrongSortFieldsException();
            }
        }
    }

}