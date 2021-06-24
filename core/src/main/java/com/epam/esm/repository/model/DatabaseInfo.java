package com.epam.esm.repository.model;

public final class DatabaseInfo {
    public static final String TAG_TABLE = "tags";
    public static final String CERTIFICATE_TABLE = "gift_certificates";
    public static final String USER_TABLE = "users";
    public static final String ORDER_TABLE = "orders";
    public static final String CERTIFICATE_CREATE_DATE_COLUMN = "create_date";
    public static final String CERTIFICATE_LAST_UPDATE_COLUMN = "last_update_date";
    public static final String CERTIFICATE_TAG_TABLE = "certificate_tag";
    public static final String CERTIFICATE_ID_COLUMN = "certificate_id";
    public static final String USER_ID_COLUMN = "user_id";
    public static final String ORDER_ID_COLUMN = "order_id";
    public static final String PURCHASE_DATE_COLUMN = "purchase_date";
    public static final String CERTIFICATE_ORDER_TABLE = "certificate_order";
    public static final String TAG_ID_COLUMN = "tag_id";

    private DatabaseInfo() {
    }
}
