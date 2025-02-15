package raido.loadtest.config;

import com.auth0.jwt.algorithms.Algorithm;
import raido.apisvc.endpoint.raidv2.AuthzUtil;
import raido.apisvc.service.auth.RaidV2ApiKeyApiTokenService;
import raido.db.jooq.api_svc.enums.UserRole;
import raido.apisvc.service.stub.util.IdFactory;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static raido.apisvc.spring.security.raidv2.ApiToken.ApiTokenBuilder.anApiToken;
import static raido.db.jooq.api_svc.enums.IdProvider.RAIDO_API;
import static raido.loadtest.config.GatlingRaidoServerConfig.serverConfig;

public class ApiKey {
  private static String BOOTSTRAP_API_TOKEN;


  // currently hardcoded to the key used in DEMO for legacy migration
  private static long API_KEY_ID = 1000000001;
  
  private static final IdFactory idFactory = new IdFactory("load-api");
  
  @SuppressWarnings("SameParameterValue")
  private static String generateApiToken(
    Algorithm signingAlgo, 
    long svcPointId, 
    String subject, 
    UserRole role
  ) {
    LocalDateTime expiry = LocalDateTime.now().plusDays(30);

    var apiToken = RaidV2ApiKeyApiTokenService.sign(
      signingAlgo,
      anApiToken().
        withAppUserId(API_KEY_ID).
        withServicePointId(svcPointId).
        withSubject(subject).
        withClientId(RAIDO_API.getLiteral()).
        withEmail(subject).
        withRole(role.getLiteral()).
        build(),
      expiry.toInstant(ZoneOffset.UTC),
      serverConfig.apiTokenIssuer);

    return apiToken;
  }

  /* static sync because I'm worried about this being called from multiple 
  threads simultaneously at startup.
  But... who really cares? So a few would be thrown away until the value becomes
  visible to other threads - just mark the static member volatile and don't 
  worry about it. 
  OTOH, who cares if it's sync - we should not be creating lots of bootstrap
  tokens, so it doesn't matter if it's synchronous. 
  */
  public static synchronized String bootstrapApiToken(){
    if( BOOTSTRAP_API_TOKEN != null ){
      return BOOTSTRAP_API_TOKEN;
    }
    var signingAlgo = Algorithm.HMAC256(serverConfig.apiKeyJwtSecret);
    BOOTSTRAP_API_TOKEN = generateApiToken( signingAlgo, 
      AuthzUtil.RAIDO_SP_ID,
      idFactory.generateUniqueId(),
      UserRole.OPERATOR );

    return BOOTSTRAP_API_TOKEN;
  }
  
}
