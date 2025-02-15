/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.api_svc.tables;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function11;
import org.jooq.Index;
import org.jooq.JSONB;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row11;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import raido.db.jooq.api_svc.ApiSvc;
import raido.db.jooq.api_svc.Indexes;
import raido.db.jooq.api_svc.Keys;
import raido.db.jooq.api_svc.enums.Metaschema;
import raido.db.jooq.api_svc.tables.records.RaidRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Raid extends TableImpl<RaidRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>api_svc.raid</code>
     */
    public static final Raid RAID = new Raid();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RaidRecord> getRecordType() {
        return RaidRecord.class;
    }

    /**
     * The column <code>api_svc.raid.handle</code>. Holds the handle (i.e. just
     * prefix/suffix) not the URL.  Usually quite  short in production, but the
     * max length is set to accommodate int and load testing.
     */
    public final TableField<RaidRecord, String> HANDLE = createField(DSL.name("handle"), SQLDataType.VARCHAR(128).nullable(false), this, "Holds the handle (i.e. just prefix/suffix) not the URL.  Usually quite  short in production, but the max length is set to accommodate int and load testing.");

    /**
     * The column <code>api_svc.raid.service_point_id</code>.
     */
    public final TableField<RaidRecord, Long> SERVICE_POINT_ID = createField(DSL.name("service_point_id"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>api_svc.raid.url</code>. The value that we set as the
     * `URL` property via ARDC APIDS.
     *   Example: `https://demo.raido-infra.com/raid/123.456/789`. 
     *   The global handle regisrty url (e.g.
     * `https://hdl.handle.net/123.456/789`) 
     *   will redirect to this value.
     */
    public final TableField<RaidRecord, String> URL = createField(DSL.name("url"), SQLDataType.VARCHAR(512).nullable(false), this, "The value that we set as the `URL` property via ARDC APIDS.\n  Example: `https://demo.raido-infra.com/raid/123.456/789`. \n  The global handle regisrty url (e.g. `https://hdl.handle.net/123.456/789`) \n  will redirect to this value.");

    /**
     * The column <code>api_svc.raid.url_index</code>. The `index` of the URL
     * property in APIDS. This can be different if we change
     *   how we mint URL values via APIDS.
     */
    public final TableField<RaidRecord, Integer> URL_INDEX = createField(DSL.name("url_index"), SQLDataType.INTEGER.nullable(false), this, "The `index` of the URL property in APIDS. This can be different if we change\n  how we mint URL values via APIDS.");

    /**
     * The column <code>api_svc.raid.primary_title</code>.
     */
    public final TableField<RaidRecord, String> PRIMARY_TITLE = createField(DSL.name("primary_title"), SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>api_svc.raid.confidential</code>.
     */
    public final TableField<RaidRecord, Boolean> CONFIDENTIAL = createField(DSL.name("confidential"), SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>api_svc.raid.metadata_schema</code>. Identifies the
     * structure of the data in the metadata column
     */
    public final TableField<RaidRecord, Metaschema> METADATA_SCHEMA = createField(DSL.name("metadata_schema"), SQLDataType.VARCHAR.nullable(false).asEnumDataType(raido.db.jooq.api_svc.enums.Metaschema.class), this, "Identifies the structure of the data in the metadata column");

    /**
     * The column <code>api_svc.raid.metadata</code>.
     */
    public final TableField<RaidRecord, JSONB> METADATA = createField(DSL.name("metadata"), SQLDataType.JSONB.nullable(false), this, "");

    /**
     * The column <code>api_svc.raid.start_date</code>.
     */
    public final TableField<RaidRecord, LocalDate> START_DATE = createField(DSL.name("start_date"), SQLDataType.LOCALDATE.nullable(false).defaultValue(DSL.field(DSL.raw("transaction_timestamp()"), SQLDataType.LOCALDATE)), this, "");

    /**
     * The column <code>api_svc.raid.date_created</code>.
     */
    public final TableField<RaidRecord, LocalDateTime> DATE_CREATED = createField(DSL.name("date_created"), SQLDataType.LOCALDATETIME(6).nullable(false).defaultValue(DSL.field(DSL.raw("transaction_timestamp()"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>api_svc.raid.version</code>.
     */
    public final TableField<RaidRecord, Integer> VERSION = createField(DSL.name("version"), SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("1"), SQLDataType.INTEGER)), this, "");

    private Raid(Name alias, Table<RaidRecord> aliased) {
        this(alias, aliased, null);
    }

    private Raid(Name alias, Table<RaidRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>api_svc.raid</code> table reference
     */
    public Raid(String alias) {
        this(DSL.name(alias), RAID);
    }

    /**
     * Create an aliased <code>api_svc.raid</code> table reference
     */
    public Raid(Name alias) {
        this(alias, RAID);
    }

    /**
     * Create a <code>api_svc.raid</code> table reference
     */
    public Raid() {
        this(DSL.name("raid"), null);
    }

    public <O extends Record> Raid(Table<O> child, ForeignKey<O, RaidRecord> key) {
        super(child, key, RAID);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ApiSvc.API_SVC;
    }

    @Override
    public List<Index> getIndexes() {
        return Arrays.asList(Indexes.IDX_RAID_SERVICE_POINT_ID_DATE_CREATED);
    }

    @Override
    public UniqueKey<RaidRecord> getPrimaryKey() {
        return Keys.RAID_PKEY;
    }

    @Override
    public List<ForeignKey<RaidRecord, ?>> getReferences() {
        return Arrays.asList(Keys.RAID__RAID_SERVICE_POINT_ID_FKEY);
    }

    private transient ServicePoint _servicePoint;

    /**
     * Get the implicit join path to the <code>api_svc.service_point</code>
     * table.
     */
    public ServicePoint servicePoint() {
        if (_servicePoint == null)
            _servicePoint = new ServicePoint(this, Keys.RAID__RAID_SERVICE_POINT_ID_FKEY);

        return _servicePoint;
    }

    @Override
    public Raid as(String alias) {
        return new Raid(DSL.name(alias), this);
    }

    @Override
    public Raid as(Name alias) {
        return new Raid(alias, this);
    }

    @Override
    public Raid as(Table<?> alias) {
        return new Raid(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Raid rename(String name) {
        return new Raid(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Raid rename(Name name) {
        return new Raid(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Raid rename(Table<?> name) {
        return new Raid(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row11 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row11<String, Long, String, Integer, String, Boolean, Metaschema, JSONB, LocalDate, LocalDateTime, Integer> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function11<? super String, ? super Long, ? super String, ? super Integer, ? super String, ? super Boolean, ? super Metaschema, ? super JSONB, ? super LocalDate, ? super LocalDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function11<? super String, ? super Long, ? super String, ? super Integer, ? super String, ? super Boolean, ? super Metaschema, ? super JSONB, ? super LocalDate, ? super LocalDateTime, ? super Integer, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
