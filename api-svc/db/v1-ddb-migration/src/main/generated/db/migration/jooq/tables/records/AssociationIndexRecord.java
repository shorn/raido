/*
 * This file is generated by jOOQ.
 */
package db.migration.jooq.tables.records;


import db.migration.jooq.tables.AssociationIndex;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.JSONB;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * imported from
 * arn:aws:dynamodb:ap-southeast-2:005299621378:table/RAiD-RAiDLiveDB-1SX7NYTSOSUKX-AssociationIndexTable-1EMNYHDPK9NBP
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AssociationIndexRecord extends UpdatableRecordImpl<AssociationIndexRecord> implements Record7<String, String, String, String, String, LocalDateTime, JSONB> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>raid_v1_import.association_index.handle</code>.
     */
    public AssociationIndexRecord setHandle(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.handle</code>.
     */
    public String getHandle() {
        return (String) get(0);
    }

    /**
     * Setter for <code>raid_v1_import.association_index.owner_name</code>.
     */
    public AssociationIndexRecord setOwnerName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.owner_name</code>.
     */
    public String getOwnerName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>raid_v1_import.association_index.raid_name</code>.
     */
    public AssociationIndexRecord setRaidName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.raid_name</code>.
     */
    public String getRaidName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>raid_v1_import.association_index.role</code>. `owner` -
     * if type is `service`, otherwise not set
     */
    public AssociationIndexRecord setRole(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.role</code>. `owner` -
     * if type is `service`, otherwise not set
     */
    public String getRole() {
        return (String) get(3);
    }

    /**
     * Setter for <code>raid_v1_import.association_index.type</code>.
     * `service|institution` - 14K service, institution only 16
     */
    public AssociationIndexRecord setType(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.type</code>.
     * `service|institution` - 14K service, institution only 16
     */
    public String getType() {
        return (String) get(4);
    }

    /**
     * Setter for <code>raid_v1_import.association_index.start_date</code>.
     */
    public AssociationIndexRecord setStartDate(LocalDateTime value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.start_date</code>.
     */
    public LocalDateTime getStartDate() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>raid_v1_import.association_index.s3_export</code>.
     */
    public AssociationIndexRecord setS3Export(JSONB value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>raid_v1_import.association_index.s3_export</code>.
     */
    public JSONB getS3Export() {
        return (JSONB) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row7<String, String, String, String, String, LocalDateTime, JSONB> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    public Row7<String, String, String, String, String, LocalDateTime, JSONB> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return AssociationIndex.ASSOCIATION_INDEX.HANDLE;
    }

    @Override
    public Field<String> field2() {
        return AssociationIndex.ASSOCIATION_INDEX.OWNER_NAME;
    }

    @Override
    public Field<String> field3() {
        return AssociationIndex.ASSOCIATION_INDEX.RAID_NAME;
    }

    @Override
    public Field<String> field4() {
        return AssociationIndex.ASSOCIATION_INDEX.ROLE;
    }

    @Override
    public Field<String> field5() {
        return AssociationIndex.ASSOCIATION_INDEX.TYPE;
    }

    @Override
    public Field<LocalDateTime> field6() {
        return AssociationIndex.ASSOCIATION_INDEX.START_DATE;
    }

    @Override
    public Field<JSONB> field7() {
        return AssociationIndex.ASSOCIATION_INDEX.S3_EXPORT;
    }

    @Override
    public String component1() {
        return getHandle();
    }

    @Override
    public String component2() {
        return getOwnerName();
    }

    @Override
    public String component3() {
        return getRaidName();
    }

    @Override
    public String component4() {
        return getRole();
    }

    @Override
    public String component5() {
        return getType();
    }

    @Override
    public LocalDateTime component6() {
        return getStartDate();
    }

    @Override
    public JSONB component7() {
        return getS3Export();
    }

    @Override
    public String value1() {
        return getHandle();
    }

    @Override
    public String value2() {
        return getOwnerName();
    }

    @Override
    public String value3() {
        return getRaidName();
    }

    @Override
    public String value4() {
        return getRole();
    }

    @Override
    public String value5() {
        return getType();
    }

    @Override
    public LocalDateTime value6() {
        return getStartDate();
    }

    @Override
    public JSONB value7() {
        return getS3Export();
    }

    @Override
    public AssociationIndexRecord value1(String value) {
        setHandle(value);
        return this;
    }

    @Override
    public AssociationIndexRecord value2(String value) {
        setOwnerName(value);
        return this;
    }

    @Override
    public AssociationIndexRecord value3(String value) {
        setRaidName(value);
        return this;
    }

    @Override
    public AssociationIndexRecord value4(String value) {
        setRole(value);
        return this;
    }

    @Override
    public AssociationIndexRecord value5(String value) {
        setType(value);
        return this;
    }

    @Override
    public AssociationIndexRecord value6(LocalDateTime value) {
        setStartDate(value);
        return this;
    }

    @Override
    public AssociationIndexRecord value7(JSONB value) {
        setS3Export(value);
        return this;
    }

    @Override
    public AssociationIndexRecord values(String value1, String value2, String value3, String value4, String value5, LocalDateTime value6, JSONB value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AssociationIndexRecord
     */
    public AssociationIndexRecord() {
        super(AssociationIndex.ASSOCIATION_INDEX);
    }

    /**
     * Create a detached, initialised AssociationIndexRecord
     */
    public AssociationIndexRecord(String handle, String ownerName, String raidName, String role, String type, LocalDateTime startDate, JSONB s3Export) {
        super(AssociationIndex.ASSOCIATION_INDEX);

        setHandle(handle);
        setOwnerName(ownerName);
        setRaidName(raidName);
        setRole(role);
        setType(type);
        setStartDate(startDate);
        setS3Export(s3Export);
    }
}
