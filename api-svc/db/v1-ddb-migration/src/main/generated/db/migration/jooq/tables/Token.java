/*
 * This file is generated by jOOQ.
 */
package db.migration.jooq.tables;


import db.migration.jooq.Keys;
import db.migration.jooq.RaidV1Import;
import db.migration.jooq.tables.records.TokenRecord;

import java.time.LocalDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function5;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row5;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * from
 * arn:aws:dynamodb:ap-southeast-2:005299621378:table/RAiD-TokenTable-1P6MFZ0WFEETH
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Token extends TableImpl<TokenRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>raid_v1_import.token</code>
     */
    public static final Token TOKEN = new Token();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TokenRecord> getRecordType() {
        return TokenRecord.class;
    }

    /**
     * The column <code>raid_v1_import.token.name</code>.
     */
    public final TableField<TokenRecord, String> NAME = createField(DSL.name("name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>raid_v1_import.token.environment</code>.
     */
    public final TableField<TokenRecord, String> ENVIRONMENT = createField(DSL.name("environment"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>raid_v1_import.token.date_created</code>.
     */
    public final TableField<TokenRecord, LocalDateTime> DATE_CREATED = createField(DSL.name("date_created"), SQLDataType.LOCALDATETIME(6).nullable(false), this, "");

    /**
     * The column <code>raid_v1_import.token.token</code>.
     */
    public final TableField<TokenRecord, String> TOKEN_ = createField(DSL.name("token"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>raid_v1_import.token.s3_export</code>.
     */
    public final TableField<TokenRecord, JSONB> S3_EXPORT = createField(DSL.name("s3_export"), SQLDataType.JSONB.nullable(false), this, "");

    private Token(Name alias, Table<TokenRecord> aliased) {
        this(alias, aliased, null);
    }

    private Token(Name alias, Table<TokenRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("from arn:aws:dynamodb:ap-southeast-2:005299621378:table/RAiD-TokenTable-1P6MFZ0WFEETH"), TableOptions.table());
    }

    /**
     * Create an aliased <code>raid_v1_import.token</code> table reference
     */
    public Token(String alias) {
        this(DSL.name(alias), TOKEN);
    }

    /**
     * Create an aliased <code>raid_v1_import.token</code> table reference
     */
    public Token(Name alias) {
        this(alias, TOKEN);
    }

    /**
     * Create a <code>raid_v1_import.token</code> table reference
     */
    public Token() {
        this(DSL.name("token"), null);
    }

    public <O extends Record> Token(Table<O> child, ForeignKey<O, TokenRecord> key) {
        super(child, key, TOKEN);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : RaidV1Import.RAID_V1_IMPORT;
    }

    @Override
    public UniqueKey<TokenRecord> getPrimaryKey() {
        return Keys.TOKEN_PKEY;
    }

    @Override
    public Token as(String alias) {
        return new Token(DSL.name(alias), this);
    }

    @Override
    public Token as(Name alias) {
        return new Token(alias, this);
    }

    @Override
    public Token as(Table<?> alias) {
        return new Token(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(String name) {
        return new Token(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(Name name) {
        return new Token(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Token rename(Table<?> name) {
        return new Token(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, String, LocalDateTime, String, JSONB> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function5<? super String, ? super String, ? super LocalDateTime, ? super String, ? super JSONB, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function5<? super String, ? super String, ? super LocalDateTime, ? super String, ? super JSONB, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
