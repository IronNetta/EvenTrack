package org.seba.eventrack.il.requests;

import java.util.List;
import java.util.Map;

public record SearchParam<T>(
        String field,
        SearchOperator op,
        Object value
) {

    public static <T> SearchParam<T> create(Map.Entry<String,String> entry) {
        String field;
        SearchOperator op;
        Object value;

        String[] key = entry.getKey().split("_");
        if(key.length == 1) {
            field = key[0];
            op = SearchOperator.EQ;
        } else {
            op = SearchOperator.valueOf(key[0].toUpperCase());
            field = key[1];
        }
        value = entry.getValue();

        return new SearchParam<T>(field,op,value);
    }

    public static <T> List<SearchParam<T>> create(Map<String,String> routeParams) {
        return routeParams.entrySet().stream()
                .filter(entry -> !entry.getKey().equals("page") && !entry.getKey().equals("size") && !entry.getKey().equals("sort"))
                .map(SearchParam::<T>create)
                .toList();
    }
}
