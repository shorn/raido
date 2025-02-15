/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.raid_v1_import.tables;


import java.time.LocalDateTime;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import raido.db.jooq.raid_v1_import.RaidV1Import;
import raido.db.jooq.raid_v1_import.tables.records.ImportHistoryRecord;


/**
 * history of data import runs
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ImportHistory extends TableImpl<ImportHistoryRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>raid_v1_import.import_history</code>
     */
    public static final ImportHistory IMPORT_HISTORY = new ImportHistory();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ImportHistoryRecord> getRecordType() {
        return ImportHistoryRecord.class;
    }

    /**
     * The column <code>raid_v1_import.import_history.action_date</code>.
     */
    public final TableField<ImportHistoryRecord, LocalDateTime> ACTION_DATE = createField(DSL.name("action_date"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>raid_v1_import.import_history.status</code>.
     */
    public final TableField<ImportHistoryRecord, String> STATUS = createField(DSL.name("status"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>raid_v1_import.import_history.error</code>.
     */
    public final TableField<ImportHistoryRecord, String> ERROR = createField(DSL.name("error"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>raid_v1_import.import_history.details</code>.
     */
    public final TableField<ImportHistoryRecord, JSONB> DETAILS = createField(DSL.name("details"), SQLDataType.JSONB.nullable(false), this, "");

    private ImportHistory(Name alias, Table<ImportHistoryRecord> aliased) {
        this(alias, aliased, null);
    }

    private ImportHistory(Name alias, Table<ImportHistoryRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment("history of data import runs"), TableOptions.table());
    }

    /**
     * Create an aliased <code>raid_v1_import.import_history</code> table
     * reference
     */
    public ImportHistory(String alias) {
        this(DSL.name(alias), IMPORT_HISTORY);
    }

    /**
     * Create an aliased <code>raid_v1_import.import_history</code> table
     * reference
     */
    public ImportHistory(Name alias) {
        this(alias, IMPORT_HISTORY);
    }

    /**
     * Create a <code>raid_v1_import.import_history</code> table reference
     */
    public ImportHistory() {
        this(DSL.name("import_history"), null);
    }

    public <O extends Record> ImportHistory(Table<O> child, ForeignKey<O, ImportHistoryRecord> key) {
        super(child, key, IMPORT_HISTORY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : RaidV1Import.RAID_V1_IMPORT;
    }

    @Override
    public ImportHistory as(String alias) {
        return new ImportHistory(DSL.name(alias), this);
    }

    @Override
    public ImportHistory as(Name alias) {
        return new ImportHistory(alias, this);
    }

    @Override
    public ImportHistory as(Table<?> alias) {
        return new ImportHistory(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ImportHistory rename(String name) {
        return new ImportHistory(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ImportHistory rename(Name name) {
        return new ImportHistory(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ImportHistory rename(Table<?> name) {
        return new ImportHistory(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<LocalDateTime, String, String, JSONB> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super LocalDateTime, ? super String, ? super String, ? super JSONB, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super LocalDateTime, ? super String, ? super String, ? super JSONB, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
