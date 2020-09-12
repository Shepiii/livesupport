package de.shepiii.livesupport.queue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.shepiii.livesupport.player.SupportPlayer;

import java.util.List;

@Singleton
public final class SupportQueue {
  private final List<SupportPlayer> staffs = Lists.newCopyOnWriteArrayList();
  private final List<SupportPlayer> waiting = Lists.newCopyOnWriteArrayList();
  private SupportQueueState state = SupportQueueState.CLOSED;

  @Inject
  private SupportQueue() {

  }

  public void addStaff(SupportPlayer supportPlayer) {
    Preconditions.checkNotNull(supportPlayer);
    staffs.add(supportPlayer);
  }

  public void removeStaff(SupportPlayer supportPlayer) {
    Preconditions.checkNotNull(supportPlayer);
    staffs.remove(supportPlayer);
  }

  public void addWaiting(SupportPlayer supportPlayer) {
    Preconditions.checkNotNull(supportPlayer);
    waiting.add(supportPlayer);
  }

  public void removeWaiting(SupportPlayer supportPlayer) {
    Preconditions.checkNotNull(supportPlayer);
    waiting.remove(supportPlayer);
  }

  public void state(SupportQueueState state) {
    Preconditions.checkNotNull(state);
    this.state = state;
  }

  public List<SupportPlayer> staffs() {
    return staffs;
  }

  public List<SupportPlayer> waiting() {
    return waiting;
  }

  public SupportQueueState state() {
    return state;
  }
}
