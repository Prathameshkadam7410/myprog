package com.mycompany.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class SubscriptionDetailsSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("subscription_name", table, columnPrefix + "_subscription_name"));
        columns.add(Column.aliased("subscription_amount", table, columnPrefix + "_subscription_amount"));
        columns.add(Column.aliased("tax_amount", table, columnPrefix + "_tax_amount"));
        columns.add(Column.aliased("total_amount", table, columnPrefix + "_total_amount"));
        columns.add(Column.aliased("subscription_start_date", table, columnPrefix + "_subscription_start_date"));
        columns.add(Column.aliased("subscription_expiry_date", table, columnPrefix + "_subscription_expiry_date"));
        columns.add(Column.aliased("additional_comments", table, columnPrefix + "_additional_comments"));
        columns.add(Column.aliased("category", table, columnPrefix + "_category"));
        columns.add(Column.aliased("notification_before_expiry", table, columnPrefix + "_notification_before_expiry"));
        columns.add(Column.aliased("notification_mute_flag", table, columnPrefix + "_notification_mute_flag"));
        columns.add(Column.aliased("notification_to", table, columnPrefix + "_notification_to"));
        columns.add(Column.aliased("notification_cc", table, columnPrefix + "_notification_cc"));
        columns.add(Column.aliased("notification_bcc", table, columnPrefix + "_notification_bcc"));

        return columns;
    }
}
