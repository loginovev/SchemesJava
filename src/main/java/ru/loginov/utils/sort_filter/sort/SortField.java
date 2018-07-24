package ru.loginov.utils.sort_filter.sort;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.loginov.security.exceptions.CustomWrongSortFieldsException;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SortField {
    private String sortField;
    private int sortOrder;
}