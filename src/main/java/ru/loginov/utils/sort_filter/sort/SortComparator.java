package ru.loginov.utils.sort_filter.sort;

import java.util.Comparator;

public class SortComparator implements Comparator<Sorted> {

    private String fieldName;

    public SortComparator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public int compare(Sorted o1, Sorted o2) {
        char[] o1_Field = o1.getFieldByName(fieldName);
        char[] o2_Field = o2.getFieldByName(fieldName);


        if (o1_Field.length==0 && o2_Field.length>0){return -1;}
        if (o1_Field.length>0 && o2_Field.length==0){return 1;}

        for(int i=0;i<Math.min(o1_Field.length,o2_Field.length);i++){
            if (o1_Field[i]!=o2_Field[i]){
                if (o1_Field[i]<o2_Field[i]){return -1;}else{return 1;}
            }
        }

        if (o1_Field.length==o2_Field.length){
            return 0;
        }else{
            return o1_Field.length<o2_Field.length ? -1 : 1;
        }
    }
}