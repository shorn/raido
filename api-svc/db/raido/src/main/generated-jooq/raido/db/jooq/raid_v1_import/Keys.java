/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.raid_v1_import;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;

import raido.db.jooq.raid_v1_import.tables.AssociationIndex;
import raido.db.jooq.raid_v1_import.tables.FlywaySchemaHistory;
import raido.db.jooq.raid_v1_import.tables.Metadata;
import raido.db.jooq.raid_v1_import.tables.Raid;
import raido.db.jooq.raid_v1_import.tables.Token;
import raido.db.jooq.raid_v1_import.tables.records.AssociationIndexRecord;
import raido.db.jooq.raid_v1_import.tables.records.FlywaySchemaHistoryRecord;
import raido.db.jooq.raid_v1_import.tables.records.MetadataRecord;
import raido.db.jooq.raid_v1_import.tables.records.RaidRecord;
import raido.db.jooq.raid_v1_import.tables.records.TokenRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * raid_v1_import.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AssociationIndexRecord> ASSOCIATION_INDEX_PKEY = Internal.createUniqueKey(AssociationIndex.ASSOCIATION_INDEX, DSL.name("association_index_pkey"), new TableField[] { AssociationIndex.ASSOCIATION_INDEX.HANDLE }, true);
    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, DSL.name("flyway_schema_history_pk"), new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    public static final UniqueKey<MetadataRecord> METADATA_PKEY = Internal.createUniqueKey(Metadata.METADATA, DSL.name("metadata_pkey"), new TableField[] { Metadata.METADATA.NAME }, true);
    public static final UniqueKey<RaidRecord> RAID_PKEY = Internal.createUniqueKey(Raid.RAID, DSL.name("raid_pkey"), new TableField[] { Raid.RAID.HANDLE }, true);
    public static final UniqueKey<TokenRecord> TOKEN_PKEY = Internal.createUniqueKey(Token.TOKEN, DSL.name("token_pkey"), new TableField[] { Token.TOKEN.NAME, Token.TOKEN.ENVIRONMENT, Token.TOKEN.DATE_CREATED }, true);
}
