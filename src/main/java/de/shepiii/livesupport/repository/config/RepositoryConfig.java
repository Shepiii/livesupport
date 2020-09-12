package de.shepiii.livesupport.repository.config;

import java.util.Properties;

public final class RepositoryConfig {
  private Properties properties;

  RepositoryConfig(Properties properties) {
    this.properties = properties;
  }

  RepositoryConfig() {
  }

  public void setProperties(Properties properties) {
    this.properties = properties;
  }

  public Properties getProperties() {
    return properties;
  }
}
