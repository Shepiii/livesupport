package de.shepiii.livesupport;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.shepiii.livesupport.message.MessageConfiguration;
import de.shepiii.livesupport.message.MessageConfigurationFile;
import de.shepiii.livesupport.database.config.RepositoryFile;
import net.md_5.bungee.api.plugin.Plugin;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LiveSupportModule extends AbstractModule {
  private final Plugin plugin;
  private final Path messageConfigurationPath;
  private final Path repositoryConfiguration;
  private final Path punishStepConfig;

  private LiveSupportModule(
    Plugin plugin, Path messageConfigurationPath, Path repositoryConfiguration,
    Path punishStepConfig) {
    this.plugin = plugin;
    this.messageConfigurationPath = messageConfigurationPath;
    this.repositoryConfiguration = repositoryConfiguration;
    this.punishStepConfig = punishStepConfig;
  }

  @Override
  protected void configure() {
    bind(Plugin.class).toInstance(plugin);
  }


  @Provides
  @Singleton
  ObjectMapper provideObjectMapper(YAMLFactory yamlFactory) {
    return new ObjectMapper(yamlFactory)
      .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
      .registerModule(new Jdk8Module());
  }

  @Provides
  @Singleton
  @Named("messageConfiguration")
  Path provideMessageConfiguration() {
    return messageConfigurationPath;
  }

  @Provides
  @Singleton
  @Named("repositoryConfiguration")
  Path provideRepositoryConfiguration() {
    return repositoryConfiguration;
  }

  @Provides
  @Singleton
  @Named("punishStepConfig")
  Path providePunishStepConfig() {
    return punishStepConfig;
  }

  @Provides
  @Singleton
  MessageConfiguration provideMessageConfiguration(
    MessageConfigurationFile messageConfigurationFile
  ) throws Exception {
    return messageConfigurationFile.read();
  }

  private static final String PERSISTENCE_UNIT_NAME = "LiveSupport";

  @Singleton
  @Provides
  @PersistenceContext
  EntityManager provideEntityManager(RepositoryFile repositoryFile) throws Exception {
    Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    var entityManagerFactory =
      Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,
        repositoryFile.read().getProperties());
    return entityManagerFactory.createEntityManager();
  }

  public static LiveSupportModule create(Plugin plugin) {
    var pluginPath = plugin.getDataFolder().toPath().toString();
    var messageConfigurationPath = Paths.get(pluginPath, "messages.yml");
    var repositoryConfiguration = Paths.get(pluginPath, "repository.yml");
    var punishStepConfig = Paths.get(pluginPath, "punishStep.yml");
    return new LiveSupportModule(
      plugin, messageConfigurationPath, repositoryConfiguration, punishStepConfig
    );
  }
}
