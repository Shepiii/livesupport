package de.shepiii.livesupport.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.shepiii.livesupport.config.ConfigFile;

import java.io.IOException;
import java.nio.file.Path;

@Singleton
public final class MessageConfigurationFile extends ConfigFile<MessageConfiguration> {
  @Inject
  private MessageConfigurationFile(
    ObjectMapper objectMapper,
    @Named("messageConfiguration") Path configurationPath
  ) {
    super(objectMapper, configurationPath, MessageConfiguration.class);
  }

  @Override
  public MessageConfiguration fromTemplate() throws Exception {
    var inputStream = getClass().getClassLoader().getResourceAsStream("messages.yml");
    return objectMapper().readValue(inputStream, MessageConfiguration.class);
  }
}