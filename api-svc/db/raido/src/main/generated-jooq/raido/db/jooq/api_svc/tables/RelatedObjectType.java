/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.api_svc.tables;


import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
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
import raido.db.jooq.api_svc.Keys;
import raido.db.jooq.api_svc.tables.records.RelatedObjectTypeRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RelatedObjectType extends TableImpl<RelatedObjectTypeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>api_svc.related_object_type</code>
     */
    public static final RelatedObjectType RELATED_OBJECT_TYPE = new RelatedObjectType();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RelatedObjectTypeRecord> getRecordType() {
        return RelatedObjectTypeRecord.class;
    }

    /**
     * The column <code>api_svc.related_object_type.name</code>.
     */
    public final TableField<RelatedObjectTypeRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>api_svc.related_object_type.description</code>.
     */
    public final TableField<RelatedObjectTypeRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>api_svc.related_object_type.url</code>.
     */
    public final TableField<RelatedObjectTypeRecord, String> URL = createField(DSL.name("url"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private RelatedObjectType(Name alias, Table<RelatedObjectTypeRecord> aliased) {
        this(alias, aliased, null);
    }

    private RelatedObjectType(Name alias, Table<RelatedObjectTypeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>api_svc.related_object_type</code> table
     * reference
     */
    public RelatedObjectType(String alias) {
        this(DSL.name(alias), RELATED_OBJECT_TYPE);
    }

    /**
     * Create an aliased <code>api_svc.related_object_type</code> table
     * reference
     */
    public RelatedObjectType(Name alias) {
        this(alias, RELATED_OBJECT_TYPE);
    }

    /**
     * Create a <code>api_svc.related_object_type</code> table reference
     */
    public RelatedObjectType() {
        this(DSL.name("related_object_type"), null);
    }

    public <O extends Record> RelatedObjectType(Table<O> child, ForeignKey<O, RelatedObjectTypeRecord> key) {
        super(child, key, RELATED_OBJECT_TYPE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ApiSvc.API_SVC;
    }

    @Override
    public UniqueKey<RelatedObjectTypeRecord> getPrimaryKey() {
        return Keys.RELATED_OBJECT_TYPE_PKEY;
    }

    @Override
    public RelatedObjectType as(String alias) {
        return new RelatedObjectType(DSL.name(alias), this);
    }

    @Override
    public RelatedObjectType as(Name alias) {
        return new RelatedObjectType(alias, this);
    }

    @Override
    public RelatedObjectType as(Table<?> alias) {
        return new RelatedObjectType(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public RelatedObjectType rename(String name) {
        return new RelatedObjectType(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public RelatedObjectType rename(Name name) {
        return new RelatedObjectType(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public RelatedObjectType rename(Table<?> name) {
        return new RelatedObjectType(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
