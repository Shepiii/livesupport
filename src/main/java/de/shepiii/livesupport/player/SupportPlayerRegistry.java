package de.shepiii.livesupport.player;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.UUID;

@Singleton
public final class SupportPlayerRegistry {
  private final Map<UUID, SupportPlayer> supportPlayerMap = Maps.newConcurrentMap();

  @Inject
  private SupportPlayerRegistry() {
  }

  public void addSupportPlayer(SupportPlayer supportPlayer) {
    Preconditions.checkNotNull(supportPlayer);
    supportPlayerMap.put(supportPlayer.id(), supportPlayer);
  }

  public void removeSupportPlayer(SupportPlayer supportPlayer) {
    Preconditions.checkNotNull(supportPlayer);
    supportPlayerMap.remove(supportPlayer.id());
  }

  public boolean contains(UUID id) {
    return supportPlayerMap.containsKey(id);
  }

  public SupportPlayer getSupportPlayer(UUID id) {
    return supportPlayerMap.get(id);
  }
}
