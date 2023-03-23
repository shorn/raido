/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.api_svc.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;

import raido.db.jooq.api_svc.tables.RelatedRaidType;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RelatedRaidTypeRecord extends UpdatableRecordImpl<RelatedRaidTypeRecord> implements Record3<String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>api_svc.related_raid_type.name</code>.
     */
    public RelatedRaidTypeRecord setName(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>api_svc.related_raid_type.name</code>.
     */
    public String getName() {
        return (String) get(0);
    }

    /**
     * Setter for <code>api_svc.related_raid_type.description</code>.
     */
    public RelatedRaidTypeRecord setDescription(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>api_svc.related_raid_type.description</code>.
     */
    public String getDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>api_svc.related_raid_type.url</code>.
     */
    public RelatedRaidTypeRecord setUrl(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>api_svc.related_raid_type.url</code>.
     */
    public String getUrl() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<String, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<String, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return RelatedRaidType.RELATED_RAID_TYPE.NAME;
    }

    @Override
    public Field<String> field2() {
        return RelatedRaidType.RELATED_RAID_TYPE.DESCRIPTION;
    }

    @Override
    public Field<String> field3() {
        return RelatedRaidType.RELATED_RAID_TYPE.URL;
    }

    @Override
    public String component1() {
        return getName();
    }

    @Override
    public String component2() {
        return getDescription();
    }

    @Override
    public String component3() {
        return getUrl();
    }

    @Override
    public String value1() {
        return getName();
    }

    @Override
    public String value2() {
        return getDescription();
    }

    @Override
    public String value3() {
        return getUrl();
    }

    @Override
    public RelatedRaidTypeRecord value1(String value) {
        setName(value);
        return this;
    }

    @Override
    public RelatedRaidTypeRecord value2(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public RelatedRaidTypeRecord value3(String value) {
        setUrl(value);
        return this;
    }

    @Override
    public RelatedRaidTypeRecord values(String value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RelatedRaidTypeRecord
     */
    public RelatedRaidTypeRecord() {
        super(RelatedRaidType.RELATED_RAID_TYPE);
    }

    /**
     * Create a detached, initialised RelatedRaidTypeRecord
     */
    public RelatedRaidTypeRecord(String name, String description, String url) {
        super(RelatedRaidType.RELATED_RAID_TYPE);

        setName(name);
        setDescription(description);
        setUrl(url);
    }
}
