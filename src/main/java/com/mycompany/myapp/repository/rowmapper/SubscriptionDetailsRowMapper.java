package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.SubscriptionDetails;
import io.r2dbc.spi.Row;
import java.time.Instant;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link SubscriptionDetails}, with proper type conversions.
 */
@Service
public class SubscriptionDetailsRowMapper implements BiFunction<Row, String, SubscriptionDetails> {

    private final ColumnConverter converter;

    public SubscriptionDetailsRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link SubscriptionDetails} stored in the database.
     */
    @Override
    public SubscriptionDetails apply(Row row, String prefix) {
        SubscriptionDetails entity = new SubscriptionDetails();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setSubscriptionName(converter.fromRow(row, prefix + "_subscription_name", String.class));
        entity.setSubscriptionAmount(converter.fromRow(row, prefix + "_subscription_amount", Integer.class));
        entity.setTaxAmount(converter.fromRow(row, prefix + "_tax_amount", Integer.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", Integer.class));
        entity.setSubscriptionStartDate(converter.fromRow(row, prefix + "_subscription_start_date", Instant.class));
        entity.setSubscriptionExpiryDate(converter.fromRow(row, prefix + "_subscription_expiry_date", Instant.class));
        entity.setAdditionalComments(converter.fromRow(row, prefix + "_additional_comments", String.class));
        entity.setCategory(converter.fromRow(row, prefix + "_category", String.class));
        entity.setNotificationBeforeExpiry(converter.fromRow(row, prefix + "_notification_before_expiry", Integer.class));
        entity.setNotificationMuteFlag(converter.fromRow(row, prefix + "_notification_mute_flag", Boolean.class));
        entity.setNotificationTo(converter.fromRow(row, prefix + "_notification_to", String.class));
        entity.setNotificationCc(converter.fromRow(row, prefix + "_notification_cc", String.class));
        entity.setNotificationBcc(converter.fromRow(row, prefix + "_notification_bcc", String.class));
        return entity;
    }
}
