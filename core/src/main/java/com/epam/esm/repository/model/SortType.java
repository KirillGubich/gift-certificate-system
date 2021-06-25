package com.epam.esm.repository.model;

public enum SortType {
    ASCENDING("asc"),
    DESCENDING("desc");

    private final String shortcut;

    SortType(String shortcut) {
        this.shortcut = shortcut;
    }

    public String getShortcut() {
        return shortcut;
    }

    public static SortType of(String type) {
        for (SortType sortType : SortType.values()) {
            if (sortType.getShortcut().equalsIgnoreCase(type)) {
                return sortType;
            }
        }
        return ASCENDING;
    }
}
