package raido.apisvc.spring.config.environment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AafOidcProps {

  @Value("${AafOidc.clientId:accaabfd-a7c8-4d36-9363-ea7342e24db5}")
  public String clientId;

  @Value("${AafOidc.clientSecret}")
  public String clientSecret;

  @Value("${AafOidc.tokenUrl:https://central.test.aaf.edu.au/providers/op/token}")
  public String tokenUrl;
}
