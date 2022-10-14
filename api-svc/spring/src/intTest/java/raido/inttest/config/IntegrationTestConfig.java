package raido.inttest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import raido.apisvc.spring.config.ApiConfig;
import raido.apisvc.util.Log;

import java.time.format.DateTimeFormatter;

import static raido.apisvc.util.Log.to;

@Configuration
@ComponentScan(basePackages = {
  // test config
  "raido.inttest.config", "raido.inttest.spring",
  
  // services used by tests and infra
  "raido.inttest.service", 
  
  // leverage prod configuration for inttest
  "raido.apisvc.spring.config.database", 
  "raido.apisvc.spring.config.environment"
})
/** this allows us to pick up the same props as used by the prod code 
 (for DB, jwtSecret, etc.) 
 */
@PropertySource(name = "user_config_environment",
  value = ApiConfig.ENV_PROPERTIES, ignoreResourceNotFound = true)
@PropertySource(name = "inttest_working_dir_environment",
  value = "./inttest.properties",
  ignoreResourceNotFound = true)
@PropertySource(name = "inttest_user_config_environment",
  value = "file:///${user.home}/.config/raido-v2/api-svc-inttest.properties",
  ignoreResourceNotFound = true)
public class IntegrationTestConfig {
  private static final Log log = to(IntegrationTestConfig.class);
  
  /** Without this, @Value annotation don't resolve ${} placeholders */
  @Bean
  public static PropertySourcesPlaceholderConfigurer
  propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public RestTemplate restTemplate(){
    var restTemplate = new RestTemplate();

    /* The overridden RestTemplate with overridden encodingMode has been
    removed as a bean and is called explictly by the tests that really need it.
    Most of the rest of the tests should just use a fiegn client. */

    return restTemplate;
  }

  public static RestTemplate restTemplateWithEncodingMode(){
    var restTemplate = new RestTemplate();
    /* this is because of the silly "handles contain a slash" problem.
    If I manually url encode the handle so that the slash is replaced, then
    when RestTemplate sees the `%2F` (for slash) in the path, it will 
    re-urlencode the percent symbol so we we end up with "%252F" in the path.
    */
    var defaultUriBuilderFactory = new DefaultUriBuilderFactory();
    defaultUriBuilderFactory.setEncodingMode(
      DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY );
    restTemplate.setUriTemplateHandler(defaultUriBuilderFactory);

    return restTemplate;
  }

  @Bean
  public SpringMvcContract feignContract(){
    return new SpringMvcContract();
  }

  @Bean public ObjectMapper objectMapper(){
    return new ObjectMapper().
      // copied from ApiConfig, may not be needed but doesn't hurt 
      disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).
      /* without this, Feign client gave error: 
      "Java 8 date/time type `java.time.LocalDate` not supported by default" */
      registerModule(new JavaTimeModule());
  }
  
}
