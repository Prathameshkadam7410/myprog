package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.EmailConfig;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmailConfig}, with proper type conversions.
 */
@Service
public class EmailConfigRowMapper implements BiFunction<Row, String, EmailConfig> {

    private final ColumnConverter converter;

    public EmailConfigRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EmailConfig} stored in the database.
     */
    @Override
    public EmailConfig apply(Row row, String prefix) {
        EmailConfig entity = new EmailConfig();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmailId(converter.fromRow(row, prefix + "_email_id", String.class));
        entity.setTokenString(converter.fromRow(row, prefix + "_token_string", String.class));
        return entity;
    }
}
