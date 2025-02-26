package org.seba.eventrack.il.specification;

import org.seba.eventrack.il.requests.SearchParam;
import org.springframework.data.jpa.domain.Specification;

public interface SearchSpecification {

    static <T> Specification<T> search(SearchParam<T> searchParam){
        return (root, query, cb) -> switch (searchParam.op()) {
            case EQ -> cb.equal(root.get(searchParam.field()),searchParam.value());
            case NE -> cb.notEqual(root.get(searchParam.field()),searchParam.value());
            case GT -> {
                if(!(searchParam.value() instanceof Number)) throw new RuntimeException("Value must be a number");
                yield cb.gt(root.get(searchParam.field()),(Number) searchParam.value());
            }
            case GTE -> {
                if(!(searchParam.value() instanceof Number)) throw new RuntimeException("Value must be a number");
                yield cb.ge(root.get(searchParam.field()),(Number) searchParam.value());
            }
            case LT -> {
                if(!(searchParam.value() instanceof Number)) throw new RuntimeException("Value must be a number");
                yield cb.lt(root.get(searchParam.field()),(Number) searchParam.value());
            }
            case LTE -> {
                if(!(searchParam.value() instanceof Number)) throw new RuntimeException("Value must be a number");
                yield cb.le(root.get(searchParam.field()),(Number) searchParam.value());
            }
            case START -> cb.like(root.get(searchParam.field()),searchParam.value() + "%");
            case END -> cb.like(root.get(searchParam.field()),"%" + searchParam.value());
            case CONTAINS -> cb.like(root.get(searchParam.field()),"%" + searchParam.value() + "%");
            case IN -> root.get(searchParam.field()).in(((String)searchParam.value()).split(","));
            case NIN -> cb.not(root.get(searchParam.field())).in(((String)searchParam.value()).split(","));
        };
    }
}
