package raido.apisvc.endpoint.raidv2;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import raido.apisvc.endpoint.Constant;
import raido.apisvc.service.auth.RaidV2ApiKeyAuthService;
import raido.apisvc.service.auth.admin.AppUserService;
import raido.apisvc.service.auth.admin.AuthzRequestService;
import raido.apisvc.service.auth.admin.ServicePointService;
import raido.apisvc.util.Guard;
import raido.apisvc.util.Log;
import raido.db.jooq.api_svc.enums.IdProvider;
import raido.db.jooq.api_svc.tables.records.AppUserRecord;
import raido.idl.raidv2.api.AdminExperimentalApi;
import raido.idl.raidv2.model.ApiKey;
import raido.idl.raidv2.model.AppUser;
import raido.idl.raidv2.model.AppUserExtraV1;
import raido.idl.raidv2.model.AuthzRequestExtraV1;
import raido.idl.raidv2.model.GenerateApiTokenRequest;
import raido.idl.raidv2.model.GenerateApiTokenResponse;
import raido.idl.raidv2.model.ServicePoint;
import raido.idl.raidv2.model.UpdateAuthzRequestStatus;

import java.time.ZoneOffset;
import java.util.List;

import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static raido.apisvc.endpoint.message.RaidApiMessage.CANT_GENERATE_DISABLED_KEY;
import static raido.apisvc.endpoint.message.RaidApiMessage.NO_APP_USER_WITH_API_KEY_ENDPOINT;
import static raido.apisvc.endpoint.raidv2.AuthzUtil.guardOperatorOrAssociated;
import static raido.apisvc.endpoint.raidv2.AuthzUtil.guardOperatorOrAssociatedSpAdmin;
import static raido.apisvc.endpoint.raidv2.AuthzUtil.guardOperatorOrSpAdmin;
import static raido.apisvc.endpoint.raidv2.AuthzUtil.isOperatorOrSpAdmin;
import static raido.apisvc.service.auth.AuthzTokenPayload.AuthzTokenPayloadBuilder.anAuthzTokenPayload;
import static raido.apisvc.util.ExceptionUtil.iae;
import static raido.apisvc.util.Log.to;
import static raido.apisvc.util.ObjectUtil.areEqual;
import static raido.db.jooq.api_svc.enums.UserRole.OPERATOR;
import static raido.db.jooq.api_svc.tables.AppUser.APP_USER;
import static raido.db.jooq.api_svc.tables.ServicePoint.SERVICE_POINT;
import static raido.db.jooq.api_svc.tables.UserAuthzRequest.USER_AUTHZ_REQUEST;

@Scope(proxyMode = TARGET_CLASS)
@RestController
@Transactional
public class AdminExperimental implements AdminExperimentalApi {
  private static final Log log = to(AdminExperimental.class);
  
  private AuthzRequestService authzRequestSvc;
  private ServicePointService servicePointSvc;
  private AppUserService appUserSvc;
  private DSLContext db;
  private RaidV2ApiKeyAuthService apiAuthSvc;

  public AdminExperimental(
    AuthzRequestService authzRequestSvc,
    ServicePointService servicePointSvc,
    AppUserService appUserSvc, 
    DSLContext db,
    RaidV2ApiKeyAuthService apiAuthSvc
  ) {
    this.authzRequestSvc = authzRequestSvc;
    this.servicePointSvc = servicePointSvc;
    this.appUserSvc = appUserSvc;
    this.db = db;
    this.apiAuthSvc = apiAuthSvc;
  }

  @Override
  public List<AuthzRequestExtraV1> listAuthzRequest() {
    var user = AuthzUtil.getAuthzPayload();
    // this is the authz check, will be moved to a role annotation soon
    Guard.areEqual(user.getRole(), OPERATOR.getLiteral());

    return authzRequestSvc.listAllRecentAuthzRequest();
  }

  @Override
  public AuthzRequestExtraV1 readRequestAuthz(Long authzRequestId) {
    // have to read it before we can see if user is allowed for servicePoint 
    var authRequest = authzRequestSvc.readAuthzRequest(authzRequestId);
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrAssociatedSpAdmin(user, authRequest.getServicePointId());
    return authRequest;
  }

  @Override
  public Void updateAuthzRequestStatus(UpdateAuthzRequestStatus req) {
    var user = AuthzUtil.getAuthzPayload();
    
    var authzRecord = db.fetchSingle(
      USER_AUTHZ_REQUEST, 
      USER_AUTHZ_REQUEST.ID.eq(req.getAuthzRequestId()) );

    guardOperatorOrAssociatedSpAdmin(user, authzRecord.getServicePointId());

    authzRequestSvc.updateAuthzRequestStatus(user, req, authzRecord);

    return null;
  }

  @Override
  public List<ServicePoint> listServicePoint() {
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrSpAdmin(user);
    
    return db.select().from(SERVICE_POINT).
      orderBy(SERVICE_POINT.NAME.asc()).
      limit(Constant.MAX_EXPERIMENTAL_RECORDS).
      fetchInto(ServicePoint.class);
  }

  /** IMPROVE: Currently gives a 500 error if not found, 404 might be better? */
  @Override
  public ServicePoint readServicePoint(Long servicePointId) {
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrAssociated(user, servicePointId);
    
    return db.select().from(SERVICE_POINT).
      where(SERVICE_POINT.ID.eq(servicePointId)).
      fetchSingleInto(ServicePoint.class);
  }

  @Override
  public ServicePoint updateServicePoint(ServicePoint req) {
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrAssociatedSpAdmin(user, req.getId());
    
    Guard.notNull(req);
    Guard.hasValue("must have a name", req.getName());
    Guard.notNull("must have adminEmail", req.getAdminEmail());
    Guard.notNull("must have techEmail", req.getTechEmail());
    Guard.notNull("must have a enabled flag", req.getEnabled());

    return servicePointSvc.updateServicePoint(req);
  }

  @Override
  public AppUser readAppUser(Long appUserId) {
    var user = AuthzUtil.getAuthzPayload();
    if( areEqual(user.getAppUserId(), appUserId) ){
      // user is allowed to read their own record
    }
    else if( isOperatorOrSpAdmin(user) ){
      /* operators or spAdmin can read info about any user in any service 
      point, spAdmin might be looking at a user that was approved onto a 
      different service point. */
    }
    else {
      var iae = iae("user read not allowed");
      log.with("user", user).with("appUserId", appUserId).
        error(iae.getMessage());
      throw iae;
    }
    
    return appUserSvc.readAppUser(appUserId);
  }

  @Override
  public AppUserExtraV1 readAppUserExtra(Long appUserId) {
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrSpAdmin(user);

    var appUser = readAppUser(appUserId);
    var servicePoint = readServicePoint(appUser.getServicePointId());
    
    var authzRequest = authzRequestSvc.readAuthzRequestForUser(appUser);
    
    // bootstrapped user has no authzRequest, was auto-approved
    if( authzRequest.isEmpty() ){
      return new AppUserExtraV1().
        appUser(appUser).
        servicePoint(servicePoint);
    }

    log.with("authzRequest", authzRequest).debug("authzReqeust");
    return new AppUserExtraV1().
      appUser(appUser).
      servicePoint(servicePoint).
      authzRequest(authzRequest.get());
    
  }

  @Override
  public List<AppUser> listAppUser(Long servicePointId) {
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrAssociatedSpAdmin(user, servicePointId);

    return appUserSvc.listAppUser(servicePointId);
  }

  @Override
  public AppUser updateAppUser(AppUser req) {
    var invokingUser = AuthzUtil.getAuthzPayload();

    var targetUser = db.fetchSingle(APP_USER, APP_USER.ID.eq(req.getId()) );

    // spAdmin can only edit users in their associated SP 
    guardOperatorOrAssociatedSpAdmin(
      invokingUser, targetUser.getServicePointId());

    appUserSvc.updateAppUser(req, invokingUser, targetUser);

    return readAppUser(targetUser.getId());
  }

  @Override
  public List<ApiKey> listApiKey(Long servicePointId) {
    var user = AuthzUtil.getAuthzPayload();
    guardOperatorOrAssociatedSpAdmin(user, servicePointId);

    return appUserSvc.listApiKey(servicePointId);
  }

  @Override
  public ApiKey updateApiKey(ApiKey req) {
    var invokingUser = AuthzUtil.getAuthzPayload();
    guardOperatorOrAssociatedSpAdmin(invokingUser, req.getServicePointId());
    
    long id = appUserSvc.updateApiKey(req, invokingUser);
    
    return readApiKey(id);
  }

  @Override
  public ApiKey readApiKey(Long apiKeyId) {
    var invokingUser = AuthzUtil.getAuthzPayload();
    ApiKey apiKey = appUserSvc.readApiKey(apiKeyId);
    guardOperatorOrAssociatedSpAdmin(invokingUser, apiKey.getServicePointId());
    return apiKey;
  }

  @Override
  public GenerateApiTokenResponse generateApiToken(
    GenerateApiTokenRequest req
  ) {
    var invokingUser = AuthzUtil.getAuthzPayload();
    AppUserRecord apiKey = db.
      fetchSingle(APP_USER, APP_USER.ID.eq(req.getApiKeyId()));
    guardOperatorOrAssociatedSpAdmin(invokingUser, apiKey.getServicePointId());
   
    if( apiKey.getIdProvider() != IdProvider.RAIDO_API ){
      var iae = iae(NO_APP_USER_WITH_API_KEY_ENDPOINT);
      log.with("appUserIdProvider", apiKey.getIdProvider()).
        error(iae.getMessage());
      throw iae;
    }

    if( !apiKey.getEnabled() ){
      var iae = iae(CANT_GENERATE_DISABLED_KEY);
      log.with("appUserIdProvider", apiKey.getIdProvider()).
        error(iae.getMessage());
      throw iae;
    }


    var apiToken = apiAuthSvc.sign(
      anAuthzTokenPayload().
        withAppUserId(apiKey.getId()).
        withServicePointId(apiKey.getServicePointId()).
        withSubject(apiKey.getSubject()).
        withClientId(apiKey.getClientId()).
        withEmail(apiKey.getEmail()).
        withRole(apiKey.getRole().getLiteral()).
        build(),
      apiKey.getTokenCutoff().toInstant(ZoneOffset.UTC)
    );
    
    return new GenerateApiTokenResponse().
      apiKeyId(req.getApiKeyId()).
      apiToken(apiToken);
  }
}
