/*
 * This file is generated by jOOQ.
 */
package raido.db.jooq.api_svc;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import raido.db.jooq.DefaultCatalog;
import raido.db.jooq.api_svc.tables.ApiToken;
import raido.db.jooq.api_svc.tables.AppUser;
import raido.db.jooq.api_svc.tables.FlywaySchemaHistory;
import raido.db.jooq.api_svc.tables.Raid;
import raido.db.jooq.api_svc.tables.RaidoOperator;
import raido.db.jooq.api_svc.tables.ServicePoint;
import raido.db.jooq.api_svc.tables.UserAuthzRequest;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ApiSvc extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>api_svc</code>
     */
    public static final ApiSvc API_SVC = new ApiSvc();

    /**
     * No further instances allowed
     */
    private ApiSvc() {
        super("api_svc", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            ApiToken.API_TOKEN,
            AppUser.APP_USER,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            Raid.RAID,
            RaidoOperator.RAIDO_OPERATOR,
            ServicePoint.SERVICE_POINT,
            UserAuthzRequest.USER_AUTHZ_REQUEST
        );
    }
}
