package raido.inttest.endpoint.raidv1;

import feign.FeignException.InternalServerError;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import raido.apisvc.util.Log;
import raido.apisvc.util.RestUtil;
import raido.idl.raidv1.api.RaidV1Api;
import raido.idl.raidv1.model.RaidCreateModel;
import raido.idl.raidv1.model.RaidModel;
import raido.inttest.IntegrationTestCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static raido.apisvc.util.Log.to;
import static raido.apisvc.util.RestUtil.anonPost;
import static raido.apisvc.util.RestUtil.urlEncode;
import static raido.apisvc.util.test.BddUtil.EXPECT;
import static raido.apisvc.util.test.BddUtil.GIVEN;
import static raido.apisvc.util.test.BddUtil.THEN;
import static raido.apisvc.util.test.BddUtil.WHEN;
import static raido.inttest.config.IntegrationTestConfig.restTemplateWithEncodingMode;

public class RaidV1MintTest extends IntegrationTestCase {
  private static final Log log = to(RaidV1MintTest.class);

  public static String getForbiddenMessage(String path) {
    return """
      403 Forbidden: "{<EOL>"message":"Access Denied",<EOL>"url":"%s",<EOL>"status":"403"<EOL>}"
      """.formatted(path).trim();
  }


  @Test
  void shoudRejectAnonCalltoMint() {
    EXPECT("minting a raid without authenticating should fail");
    assertThatThrownBy(()->
      anonPost(rest, raidoApiServerUrl("/v1/raid"), "{}", Object.class)
    ).isInstanceOf(HttpClientErrorException.class).
        /* full text match to ensure there's no info leakage */
        hasMessage(getForbiddenMessage("/v1/raid"));
  }

  @Test
  void mintShouldRejectMissingContentPath() {
    EXPECT("minting a raid without a contentPath should fail");
    assertThatThrownBy(()->
      super.raidV1Client().raidPost(new RaidCreateModel())
    ).isInstanceOf(InternalServerError.class).
      hasMessageContaining("no contentPath");
  }
  
  @Test void mintAndGetHandleShoudSucceed(){
    EXPECT("minting a raid without minimal content should succeed");
    var create = new RaidCreateModel().
      contentPath("https://example.com");

    RaidV1Api raidV1 = super.raidV1Client();
    var mintResult = raidV1.raidPost(create);
    
    assertThat(mintResult).isNotNull();
    assertThat(mintResult.getHandle()).isNotBlank();
    
    var getResult = raidV1.handleRaidIdGet(mintResult.getHandle(), null);
    assertThat(mintResult).isNotNull();
    assertThat(getResult.getHandle()).isEqualTo(mintResult.getHandle());
  }
  
  
  @Test void getHandleWithEncodedSlashShouldSucceed(){
    GIVEN("raid exists");
    var raid = super.raidV1Client().raidPost(
      new RaidCreateModel().contentPath("https://example.com") );

    var rest = restTemplateWithEncodingMode();
    var encodedHandle = urlEncode(raid.getHandle());
    log.info("handle:" + raid.getHandle());
    log.info("encoded handle:" + encodedHandle);

    WHEN("get handle with encoded path");
    var getResult = RestUtil.get(rest, raidV1TestToken,
      raidoApiServerUrl("/v1/handle/" + encodedHandle), 
      RaidModel.class);
    THEN("raid should be returned");
    assertThat(getResult.getHandle()).isEqualTo(raid.getHandle());
  }
  

}

