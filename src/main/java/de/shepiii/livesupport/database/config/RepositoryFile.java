package de.shepiii.livesupport.database.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

@Singleton
public final class RepositoryFile {
  private final ObjectMapper objectMapper;
  private final Path path;

  enum FileState {
    FAILED, WORKED;
  }

  @Inject
  private RepositoryFile(
    ObjectMapper objectMapper,
    @Named("repositoryConfiguration") Path path
  ) {
    this.objectMapper = objectMapper;
    this.path = path;
  }

  public void write(RepositoryConfig repositoryConfig) throws Exception {
    Preconditions.checkNotNull(repositoryConfig);
    var file = path.toFile();
    if (fileStateFromFile(file) == FileState.FAILED) {
      throw new Exception("Can't create file");
    }
    objectMapper.writeValue(file, repositoryConfig);
  }

  public RepositoryConfig read() throws Exception {
    var file = path.toFile();
    if (fileStateFromFile(file) == FileState.FAILED) {
      throw new Exception("Can't create file");
    }
    try {
      return objectMapper.readValue(path.toFile(), RepositoryConfig.class);
    } catch (Exception exception) {
      var template = loadDefault();
      write(template);
      return template;
    }
  }

  private RepositoryConfig loadDefault() {
    var properties = new Properties();
    properties.put("javax.persistence.jdbc.url", "jdbc:mysql://127.0.0.1/livesupport?serverTimezone=UTC&autoReconnect=true");
    properties.put("javax.persistence.jdbc.user", "shepiii");
    properties.put("javax.persistence.jdbc.password", "2JxB={!V,_=D,Yw&");
    return new RepositoryConfig(properties);
  }

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
}
