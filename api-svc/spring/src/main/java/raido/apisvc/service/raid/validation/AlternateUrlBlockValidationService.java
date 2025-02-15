package raido.apisvc.service.raid.validation;

import org.springframework.stereotype.Component;
import raido.apisvc.endpoint.message.ValidationMessage;
import raido.idl.raidv2.model.AlternateUrlBlock;
import raido.idl.raidv2.model.ValidationFailure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static raido.apisvc.util.StringUtil.isBlank;

@Component
public class AlternateUrlBlockValidationService {
  public List<ValidationFailure> validateAlternateUrls(
    List<AlternateUrlBlock> urls
  ) {
    if( urls == null ){
      return Collections.emptyList();
    }

    var failures = new ArrayList<ValidationFailure>();

    for( int i = 0; i < urls.size(); i++ ){
      var iUrl = urls.get(i);

      if( isBlank(iUrl.getUrl()) ){
        failures.add(ValidationMessage.alternateUrlNotSet(i));
      }

      /* not sure yet if we want to be doing any further validation of this
      - only https, or only http/https?
      - must be a formal URI?
      - url must exist (ping if it returns a 200 response?) */
    }
    return failures;
  }
}
