package raid.v2_api_migration

import org.jooq.Record
import org.jooq.Result
import org.jooq.impl.DSL
import raid.ddb_migration.JooqExec
import raido.idl.raidv2.model.MigrateLegacyRaidRequest
import raido.idl.raidv2.model.MigrateLegacyRaidRequestMintRequest
import raido.idl.raidv2.model.ValidationFailure

import static db.migration.jooq.tables.Raid.RAID
import static java.lang.Integer.parseInt
import static raid.ddb_migration.Util.local2Offset
import static raid.v2_api_migration.RaidoApi.mapToLegacySchema

class Import1Raid {


  static void main(String[] args) {
    importMostRecentRaid(ImportAllRaids.NOTRE_DAME)
    importMostRecentRaid(ImportAllRaids.RDM)
    importMostRecentRaid(ImportAllRaids.ARDC)
    importMostRecentRaid(ImportAllRaids.UQ_IMAGING)
  }
  
  static void importMostRecentRaid(String svcPointName) {
    def exec = new JooqExec()
    var raid = null
    exec.withDb(db -> {
      raid = db.select().from(RAID).
        // trim because UW_IMAGING has a space at the end in the legacy data
        where(DSL.trim(RAID.OWNER).eq(svcPointName)).
        orderBy(RAID.CREATION_DATE.desc()).
        limit(1).fetchOne()
      assert raid : "could not find a raid to import for service point: $svcPointName"
      println "raid to import: $raid"
    })

    def raido = new RaidoApi()
    def servicePoint = raido.findServicePoint(svcPointName)
    println "service point to to import against" + servicePoint
    var migrateResult = raido.adminApi.migrateLegacyRaid(
      new MigrateLegacyRaidRequest().
        mintRequest(new MigrateLegacyRaidRequestMintRequest().
          servicePointId(servicePoint.id).
          createDate(local2Offset(raid.get(RAID.CREATION_DATE))).
          contentIndex(parseInt(raid.get(RAID.CONTENT_INDEX)))).
        metadata(mapToLegacySchema(raid))
    )
    assert migrateResult.success == true : migrateResult.getFailures()
    println migrateResult
    
  }

}

class ImportAllRaids {

  /* these must previously have been created in the DB with this exact name
     doesn't technically have to be a flyway migration, you can create the SP
     in the DB by hand and as long as the description matches, the migrate 
     will work.*/
  public static final String NOTRE_DAME = "University of Notre Dame Library"
  public static final String RDM = "RDM@UQ"
  public static final String ARDC = "Australian Research Data Commons"
  // beware: the legacy "owner" field in DDB has a space at the end 
  public static final String UQ_IMAGING = "UQ Centre for Advanced Imaging"

  static void main(String[] args) {
    importAllRaids(NOTRE_DAME)
    importAllRaids(RDM)
    importAllRaids(ARDC)
    importAllRaids(UQ_IMAGING)
  }

  static void importAllRaids(String svcPointName) {
    def exec = new JooqExec()

    def raido = new RaidoApi()
    def servicePoint = raido.findServicePoint(svcPointName)
    println "importing service point raids: " + servicePoint.name

    int successes = 0;
    Map<String, List<ValidationFailure>> failures = [:]
    Timer t = new Timer();
    t.scheduleAtFixedRate(
      ()->{
        println "... $successes successes. ${failures.size()} failures ..."
      },
      10_000,   // first run   
      10_000)  // then every    
    
    exec.withDb(db -> {
      var Result<Record> raids = db.select().from(RAID).
        where(
          // trim because UW_IMAGING has a space at the end in the legacy data
          DSL.trim(RAID.OWNER).eq(svcPointName)
        ).
        orderBy(RAID.CREATION_DATE.desc()).
        fetch()
      
      raids.stream().forEach(iRaid -> {
        var createDate = iRaid.get(RAID.CREATION_DATE)
        var iMetadata = mapToLegacySchema(iRaid)
        var migrateResult = raido.adminApi.migrateLegacyRaid(
          new MigrateLegacyRaidRequest().
            mintRequest(new MigrateLegacyRaidRequestMintRequest().
              servicePointId(servicePoint.id).
              createDate(local2Offset(iRaid.get(RAID.CREATION_DATE))).
              contentIndex(parseInt(iRaid.get(RAID.CONTENT_INDEX))).
              createDate(local2Offset(createDate)) ).
            metadata(iMetadata)
        )

        if( migrateResult.success ){
          successes++ 
        }
        else{
          failures[iMetadata.id.identifier] = migrateResult.failures
        }

        if( failures.size() > 5 && successes == 0 ){
          // untested, dunno if using `!failures` is as clever as it looks
          assert !failures : "first 5 raids have all failed, something's wrong"
        }

      })
      
      t.cancel()
      
      println "$successes successes. ${failures.size()} failures."
      if( !failures.isEmpty() ){
        println "failures for $svcPointName..."
        println failures
      }

    })
  }

}
