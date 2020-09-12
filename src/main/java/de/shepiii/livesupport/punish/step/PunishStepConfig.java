package de.shepiii.livesupport.punish.step;

import com.google.inject.Singleton;

import java.util.Map;

@Singleton
public final class PunishStepConfig {
  private Map<Integer, Long> punishSteps;

  PunishStepConfig(Map<Integer, Long> punishSteps) {
    this.punishSteps = punishSteps;
  }

  PunishStepConfig() {

  }

  public void setPunishSteps(Map<Integer, Long> punishSteps) {
    this.punishSteps = punishSteps;
  }

  public Map<Integer, Long> getPunishSteps() {
    return punishSteps;
  }
}
