package com.cities.domain.valueobject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCriteria {
    private final String name;
    private final String sortBy;
    private final String sortOrder;
    private final Integer pageSize;
    private final Integer startPage;
}
