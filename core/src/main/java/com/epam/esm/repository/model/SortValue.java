package com.epam.esm.repository.model;

public enum SortValue {
    DATE("create_date"),
    NAME("name");

    private final String fieldName;

    SortValue(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static SortValue of(String value) {
        for (SortValue sortValue : SortValue.values()) {
            if (sortValue.getFieldName().equalsIgnoreCase(value)) {
                return sortValue;
            }
        }
        return NAME;
    }
}
