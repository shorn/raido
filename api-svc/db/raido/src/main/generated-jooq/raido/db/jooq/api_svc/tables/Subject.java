/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.api_svc.tables;


import java.util.function.Function;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
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
import raido.db.jooq.api_svc.tables.records.SubjectRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Subject extends TableImpl<SubjectRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>api_svc.subject</code>
     */
    public static final Subject SUBJECT = new Subject();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<SubjectRecord> getRecordType() {
        return SubjectRecord.class;
    }

    /**
     * The column <code>api_svc.subject.id</code>.
     */
    public final TableField<SubjectRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(6).nullable(false), this, "");

    /**
     * The column <code>api_svc.subject.name</code>.
     */
    public final TableField<SubjectRecord, String> NAME = createField(DSL.name("name"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>api_svc.subject.description</code>.
     */
    public final TableField<SubjectRecord, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.CLOB, this, "");

    /**
     * The column <code>api_svc.subject.note</code>.
     */
    public final TableField<SubjectRecord, String> NOTE = createField(DSL.name("note"), SQLDataType.CLOB, this, "");

    private Subject(Name alias, Table<SubjectRecord> aliased) {
        this(alias, aliased, null);
    }

    private Subject(Name alias, Table<SubjectRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>api_svc.subject</code> table reference
     */
    public Subject(String alias) {
        this(DSL.name(alias), SUBJECT);
    }

    /**
     * Create an aliased <code>api_svc.subject</code> table reference
     */
    public Subject(Name alias) {
        this(alias, SUBJECT);
    }

    /**
     * Create a <code>api_svc.subject</code> table reference
     */
    public Subject() {
        this(DSL.name("subject"), null);
    }

    public <O extends Record> Subject(Table<O> child, ForeignKey<O, SubjectRecord> key) {
        super(child, key, SUBJECT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : ApiSvc.API_SVC;
    }

    @Override
    public UniqueKey<SubjectRecord> getPrimaryKey() {
        return Keys.SUBJECT_PKEY;
    }

    @Override
    public Subject as(String alias) {
        return new Subject(DSL.name(alias), this);
    }

    @Override
    public Subject as(Name alias) {
        return new Subject(alias, this);
    }

    @Override
    public Subject as(Table<?> alias) {
        return new Subject(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Subject rename(String name) {
        return new Subject(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Subject rename(Name name) {
        return new Subject(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Subject rename(Table<?> name) {
        return new Subject(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super String, ? super String, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
