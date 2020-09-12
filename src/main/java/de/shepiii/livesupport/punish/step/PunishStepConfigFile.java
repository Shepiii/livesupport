package de.shepiii.livesupport.punish.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import de.shepiii.livesupport.config.ConfigFile;

import java.nio.file.Path;
import java.util.Map;

@Singleton
public final class PunishStepConfigFile extends ConfigFile<PunishStepConfig> {
  @Inject
  private PunishStepConfigFile(
    ObjectMapper objectMapper,
    @Named("punishStepConfig") Path configurationPath
  ) {
    super(objectMapper, configurationPath, PunishStepConfig.class);
  }

  private static final Long dayInMillis = 1000 * 60 * 60 * 24L;
  private static final Long monthInMillis = dayInMillis * 30;

  @Override
  public PunishStepConfig fromTemplate() {
    Map<Integer, Long> punishSteps = Maps.newHashMap();
    punishSteps.put(0, dayInMillis);
    punishSteps.put(1, 7 * dayInMillis);
    punishSteps.put(2, monthInMillis);
    punishSteps.put(3, monthInMillis * 3);
    punishSteps.put(4, monthInMillis * 6);
    punishSteps.put(5, monthInMillis * 12);
    punishSteps.put(6, -1L);                   //permanent
    return new PunishStepConfig(punishSteps);
  }
}
