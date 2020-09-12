package de.shepiii.livesupport.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;

import java.io.File;
import java.nio.file.Path;

public abstract class ConfigFile<T> {
  private final ObjectMapper objectMapper;
  private final Path configurationPath;
  private final Class<T> configurationClass;

  enum FileState {
    FAILED, WORKED;
  }

  protected ConfigFile(
    ObjectMapper objectMapper, Path configurationPath, Class<T> configurationClass
  ) {
    this.objectMapper = objectMapper;
    this.configurationPath = configurationPath;
    this.configurationClass = configurationClass;
  }

  public void write(T t) throws Exception {
    Preconditions.checkNotNull(t);
    var file = configurationPath.toFile();
    if (fileStateFromFile(file) == FileState.FAILED) {
      throw new Exception("Can't create file");
    }
    objectMapper.writeValue(file, t);
  }

  public T read() throws Exception {
    var file = configurationPath.toFile();
    if (fileStateFromFile(file) == FileState.FAILED) {
      throw new Exception("Can't create file");
    }
    try {
      return objectMapper.readValue(configurationPath.toFile(), configurationClass);
    } catch (Exception exception) {
      var templateConfiguration = fromTemplate();
      write(templateConfiguration);
      return templateConfiguration;
    }
  }

  public abstract T fromTemplate() throws Exception;

  private FileState fileStateFromFile(File file) throws Exception {
    if (file.exists()) {
      return FileState.WORKED;
    }
    file.getParentFile().mkdirs();
    if (!file.createNewFile()) {
      return FileState.FAILED;
    }
    return FileState.WORKED;
  }

  public ObjectMapper objectMapper() {
    return objectMapper;
  }

  public Path configPath() {
    return configurationPath;
  }
}
