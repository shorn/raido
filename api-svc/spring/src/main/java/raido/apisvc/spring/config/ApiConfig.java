package raido.apisvc.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import raido.apisvc.spring.RequestLoggingFilter;
import raido.apisvc.spring.config.http.converter.FormProblemDetailConverter;
import raido.apisvc.spring.config.http.converter.XmlProblemDetailConverter;
import raido.apisvc.util.Log;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static raido.apisvc.util.Log.to;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
  // spring boot-up and config
  "raido.apisvc.spring", 
  // services and endpoints
  "raido.apisvc.service",
  "raido.apisvc.endpoint",
  "raido.apisvc.repository",
  "raido.apisvc.factory"
})
@PropertySources({
  /* This is NOT for you to put an `env.properties` file with credentials in the 
  source tree!
  This is a convenience to allow simple deployments to just dump the 
  uberJar and config in a directory and run the Java command from that directly.
  It's possible to maintain multiple different configurations on the same 
  machine by putting config in separate directories and executing from those 
  directories. We use Docker to encapsulate in a real setup, but this can be
  useful sometimes to run multiple configurations on the same machine. 
  */
  @PropertySource(name = "working_dir_environment",
    value = "./env.properties",
    ignoreResourceNotFound = true),
  
  /* This is where you should put credentials for standard development workflow,
  far away from the source tree, in a standard location that usually has better
  OS-level protections (permissions, etc.)
  During standard development cycle, uses hardcoded default XDG location for 
  config files. IMPROVE: use XDG_CONFIG_HOME env variable */
  @PropertySource(name = "user_config_environment",
    value = ApiConfig.ENV_PROPERTIES, ignoreResourceNotFound = true),
  @PropertySource(name = "user_config_environment2",
    value = ApiConfig.ENV_PROPERTIES2, ignoreResourceNotFound = true),
  
  /* we put secrets into a separate file so we can keep env stuff in a nice
  visible SSM String param, and secrets can be in a SecureString or even 
  in a proper secret in AWS SecretManager. Also, so we can easily log the
  env properties without risking logging secret properties. */
  @PropertySource(name = "user_config_secret",
    value = ApiConfig.SECRET_PROPERTIES, ignoreResourceNotFound = true),
  @PropertySource(name = "user_config_secret2",
    value = ApiConfig.SECRET_PROPERTIES2, ignoreResourceNotFound = true),
  
  // added to jar at build time by gradle springBoot.buildInfo config
  @PropertySource( name = "build_info",
    value = "classpath:META-INF/build-info.properties",
    ignoreResourceNotFound = true)
})
public class ApiConfig implements WebMvcConfigurer {
  /* IMPROVE:STO after everything is stabilised on the new `raido` repo name,
  delete the old raido-v2 references. */
  public static final String ENV_PROPERTIES = "file:///${user.home}/" +
    ".config/raido-v2/api-svc-env.properties";
  public static final String SECRET_PROPERTIES = "file:///${user.home}/" +
    ".config/raido-v2/api-svc-secret.properties";
  public static final String ENV_PROPERTIES2 = "file:///${user.home}/" +
    ".config/raido/api-svc-env.properties";
  public static final String SECRET_PROPERTIES2 = "file:///${user.home}/" +
    ".config/raido/api-svc-secret.properties";

  private static final Log log = to(ApiConfig.class);
  
  


  /**
   This replaces the default resolver, I was having trouble with ordering and
   besides - no reason to have the default if it shouldn't be invoked.
   */
//  @Bean
//  public HandlerExceptionResolver handlerExceptionResolver(
//    @Value("${redactErrorDetails:true}") boolean redactErrorDetails
//  ) {
//    return new RedactingExceptionResolver(redactErrorDetails);
//  }

  /** Without this, @Value annotation don't resolve ${} placeholders */
  @Bean
  public static PropertySourcesPlaceholderConfigurer
  propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  /*
   Given that we added WRITE_DATES_AS_TIMESTAMPS for this (which I think was 
   for the REST API endpoints - I don't understand why we haven't needed to 
   register JavaTimeModule for this? Like the raidMetadata mapper and the 
   mapper used by the feign client for integration tests - we had to register
   the module for those usages, why not here?
  */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().
      /* from memory, this was to get the Spring REST API endpoints writing 
      datetime the way I wanted. */
      disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).
      registerModule(new JavaTimeModule());
  }
  
  @Bean
  public static RestTemplate restTemplate(){
    MappingJackson2XmlHttpMessageConverter xmlConverter =
      new MappingJackson2XmlHttpMessageConverter();
    xmlConverter.setSupportedMediaTypes(
      singletonList(MediaType.APPLICATION_XML) );

    MappingJackson2HttpMessageConverter jsonConverter =
      new MappingJackson2HttpMessageConverter();

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    messageConverters.add(xmlConverter);
    messageConverters.add(jsonConverter);
    messageConverters.add(new FormHttpMessageConverter());

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setMessageConverters(messageConverters);

    return restTemplate;
  }

  /* Not sure if we should be using "configure" or "extend".  AFAIK, this here
  is resetting the default converters, so this converter is the only one.
  Does this mean our server doesn't support other content types?
  Not sure if that's a good thing or a bad thing. 
  Whatever else this is used for, it is used by the HttpEntityMethodProcessor
  to create the "return value" from a "ResponseEntity", so if you're having 
  conversion errors caused by weird contentTypes or accept headers, this might
  be the place you need to deal with it.
  */
  @Override
  public void configureMessageConverters(
    List<HttpMessageConverter<?>> converters
  ) {
    MappingJackson2HttpMessageConverter jsonConverter =
      new MappingJackson2HttpMessageConverter();

    /* By default dates looked like `1662077155.409857400`. 
    The app-client openapi generated ts that converted this to dates with code 
    like `new Date(json['startDate']` which did not parse the date properly.
    https://stackoverflow.com/a/67078987/924597 */
    jsonConverter.getObjectMapper().
      disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    converters.add(jsonConverter);
    
    converters.add(new FormProblemDetailConverter());
    converters.add(new XmlProblemDetailConverter());

    // prototype: used for returning static HTML from an endpoint
//    converters.add(PublicExperimental.getHtmlStringConverter());
  }
}


