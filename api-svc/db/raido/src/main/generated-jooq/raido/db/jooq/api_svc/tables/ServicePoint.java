/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.api_svc.tables;


import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.jooq.Check;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function9;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import raido.db.jooq.api_svc.ApiSvc;
import raido.db.jooq.api_svc.Keys;
import raido.db.jooq.api_svc.tables.records.ServicePointRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ServicePoint extends TableImpl<ServicePointRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>api_svc.service_point</code>
     */
    public static final ServicePoint SERVICE_POINT = new ServicePoint();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ServicePointRecord> getRecordType() {
        return ServicePointRecord.class;
    }

    /**
     * The column <code>api_svc.service_point.id</code>.
     */
    public final TableField<ServicePointRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>api_svc.service_point.name</code>.
     */
    public final TableField<ServicePointRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>api_svc.service_point.search_content</code>. Trimmed
     * lowercase only
     */
    public final TableField<ServicePointRecord, String> SEARCH_CONTENT = createField(DSL.name("search_content"), SQLDataType.VARCHAR(256), this, "Trimmed lowercase only");

    /**
     * The column <code>api_svc.service_point.admin_email</code>.
     */
    public final TableField<ServicePointRecord, String> ADMIN_EMAIL = createField(DSL.name("admin_email"), SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>api_svc.service_point.tech_email</code>.
     */
    public final TableField<ServicePointRecord, String> TECH_EMAIL = createField(DSL.name("tech_email"), SQLDataType.VARCHAR(256).nullable(false), this, "");

    /**
     * The column <code>api_svc.service_point.enabled</code>.
     */
    public final TableField<ServicePointRecord, Boolean> ENABLED = createField(DSL.name("enabled"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field(DSL.raw("true"), SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>api_svc.service_point.lower_name</code>.
     */
    public final TableField<ServicePointRecord, String> LOWER_NAME = createField(DSL.name("lower_name"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>api_svc.service_point.identifier_owner</code>.
     */
    public final TableField<ServicePointRecord, String> IDENTIFIER_OWNER = createField(DSL.name("identifier_owner"), SQLDataType.CHAR(25).nullable(false), this, "");

    /**
     * The column <code>api_svc.service_point.app_writes_enabled</code>.
     */
    public final TableField<ServicePointRecord, Boolean> APP_WRITES_ENABLED = createField(DSL.name("app_writes_enabled"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field(DSL.raw("true"), SQLDataType.BOOLEAN)), this, "");

    private ServicePoint(Name alias, Table<ServicePointRecord> aliased) {
        this(alias, aliased, null);
    }

    private ServicePoint(Name alias, Table<ServicePointRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>api_svc.service_point</code> table reference
     */
    public ServicePoint(String alias) {
        this(DSL.name(alias), SERVICE_POINT);
    }

    /**
     * Create an aliased <code>api_svc.service_point</code> table reference
     */
    public ServicePoint(Name alias) {
        this(alias, SERVICE_POINT);
    }

    /**
     * Create a <code>api_svc.service_point</code> table reference
     */
    public ServicePoint() {
        this(DSL.name("service_point"), null);
    }

    public <O extends Record> ServicePoint(Table<O> child, ForeignKey<O, ServicePointRecord> key) {
        super(child, key, SERVICE_POINT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ApiSvc.API_SVC;
    }

    @Override
    public Identity<ServicePointRecord, Long> getIdentity() {
        return (Identity<ServicePointRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<ServicePointRecord> getPrimaryKey() {
        return Keys.SERVICE_POINT_PKEY;
    }

    @Override
    public List<UniqueKey<ServicePointRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.UNIQUE_NAME);
    }

    @Override
    public List<Check<ServicePointRecord>> getChecks() {
        return Arrays.asList(
            Internal.createCheck(this, DSL.name("lowercase_search_content"), "(((search_content)::text = lower(TRIM(BOTH FROM search_content))))", true)
        );
    }

    @Override
    public ServicePoint as(String alias) {
        return new ServicePoint(DSL.name(alias), this);
    }

    @Override
    public ServicePoint as(Name alias) {
        return new ServicePoint(alias, this);
    }

    @Override
    public ServicePoint as(Table<?> alias) {
        return new ServicePoint(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public ServicePoint rename(String name) {
        return new ServicePoint(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public ServicePoint rename(Name name) {
        return new ServicePoint(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public ServicePoint rename(Table<?> name) {
        return new ServicePoint(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<Long, String, String, String, String, Boolean, String, String, Boolean> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function9<? super Long, ? super String, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super Boolean, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function9<? super Long, ? super String, ? super String, ? super String, ? super String, ? super Boolean, ? super String, ? super String, ? super Boolean, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
