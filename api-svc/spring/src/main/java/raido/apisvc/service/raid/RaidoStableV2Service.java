package raido.apisvc.service.raid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import raido.apisvc.exception.InvalidJsonException;
import raido.apisvc.exception.InvalidVersionException;
import raido.apisvc.exception.ResourceNotFoundException;
import raido.apisvc.factory.RaidRecordFactory;
import raido.apisvc.repository.RaidRepository;
import raido.apisvc.service.raid.id.IdentifierParser;
import raido.apisvc.service.raid.id.IdentifierUrl;
import raido.idl.raidv2.model.RaidSchemaV2;
import raido.idl.raidv2.model.UpdateRaidStableV2Request;

import java.util.Optional;

@Component
public class RaidoStableV2Service {
  private final IdentifierParser idParser;
  private final RaidRepository raidRepository;
  private final RaidRecordFactory raidRecordFactory;
  private final ObjectMapper objectMapper;

  public RaidoStableV2Service(final IdentifierParser idParser, final RaidRepository raidRepository, final RaidRecordFactory raidRecordFactory, final ObjectMapper objectMapper) {
    this.idParser = idParser;
    this.raidRepository = raidRepository;
    this.raidRecordFactory = raidRecordFactory;
    this.objectMapper = objectMapper;
  }

  public Optional<RaidSchemaV2> findByHandle(final String handle) {
    return raidRepository.findByHandle(handle)
      .map(raidRecord -> {
        try {
          return objectMapper.readValue(raidRecord.getMetadata().data(), RaidSchemaV2.class);
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      });
  }

  public RaidSchemaV2 updateRaid(final UpdateRaidStableV2Request raid) {
    final IdentifierUrl id;
    try {
      id = idParser.parseUrlWithException(raid.getId().getIdentifier());
    }
    catch( ValidationFailureException e ){
      // it was already validated, so this shouldn't happen
      throw new RuntimeException(e);
    }
    String handle = id.handle().format();

    final var existing = raidRepository.findByHandle(handle)
      .orElseThrow(() -> new ResourceNotFoundException(handle));

    final var raidRecord = raidRecordFactory.merge(raid, existing);

    // check update
    final var matchedRows = raidRepository.updateByHandleAndVersion(raidRecord);

    if (matchedRows == 0) {
      throw new InvalidVersionException(raid.getId().getVersion());
    }

    try {
      return objectMapper.readValue(
        raidRecord.getMetadata().data(), RaidSchemaV2.class);
    } catch (JsonProcessingException e) {
      throw new InvalidJsonException();
    }  }
}
