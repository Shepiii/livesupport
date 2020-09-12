package de.shepiii.livesupport;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.shepiii.livesupport.message.MessageRepository;
import de.shepiii.livesupport.player.SupportPlayerTrigger;
import de.shepiii.livesupport.punish.PunishLogSubCommand;
import de.shepiii.livesupport.punish.PunishSubCommand;
import de.shepiii.livesupport.punish.UnpunishIdSubCommand;
import de.shepiii.livesupport.queue.SupportQueue;
import de.shepiii.livesupport.queue.SupportQueueTrigger;
import de.shepiii.livesupport.session.SupportSessionTrigger;
import de.shepiii.livesupport.support.SupportCommand;
import de.shepiii.livesupport.support.client.LeaveSubCommand;
import de.shepiii.livesupport.support.staff.*;
import de.shepiii.livesupport.support.subcommand.SubCommandRepository;
import de.shepiii.livesupport.timer.SupportTimerTask;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class LiveSupportPlugin extends Plugin {
  private Injector injector;
  @Inject
  private SubCommandRepository subCommandRepository;
  @Inject
  private SupportQueue supportQueue;
  @Inject
  private MessageRepository messageRepository;
  @Inject
  private SupportTimerTask supportTimerTask;

  @Override
  public void onLoad() {
    injector = Guice.createInjector(LiveSupportModule.create(this));
    injector.injectMembers(this);
  }

  @Override
  public void onEnable() {
    registerCommands();
    registerTrigger();
    registerSubCommands();
    sendRegularMessageToStaff();
  }

  private void registerCommands() {
    getProxy().getPluginManager().registerCommand(
      this, injector.getInstance(SupportCommand.class));
  }

  private void registerTrigger() {
    var pluginManager = getProxy().getPluginManager();
    pluginManager.registerListener(
      this, injector.getInstance(SupportPlayerTrigger.class));
    pluginManager.registerListener(
      this, injector.getInstance(SupportQueueTrigger.class));
    pluginManager.registerListener(
      this, injector.getInstance(SupportSessionTrigger.class));
  }

  private void registerSubCommands() {
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(LoginSubCommand.class), "login");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(LogoutSubCommand.class), "logout");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(ListSubCommand.class), "list");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(NextSubCommand.class), "next");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(LeaveSubCommand.class), "leave");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(FinishSubCommand.class), "finish");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(PunishSubCommand.class), "punish");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(PunishLogSubCommand.class), "punishlog");
    subCommandRepository.addSubCommandExecutor(
      injector.getInstance(UnpunishIdSubCommand.class), "unpunishid");
  }

  private static final long delaySeconds = 30L;

  private final ExecutorService executorService = Executors.newSingleThreadExecutor();

  private void sendRegularMessageToStaff() {
    executorService.execute(() ->
      new Timer().scheduleAtFixedRate(
        supportTimerTask, delaySeconds * 1000L, delaySeconds * 1000L
      ));
  }
}
