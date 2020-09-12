package de.shepiii.livesupport.message;

import java.util.Map;

public final class MessageConfiguration {
  private Map<String, String> messages;
  private String prefix;

  MessageConfiguration(Map<String, String> messages, String prefix) {
    this.messages = messages;
    this.prefix = prefix;
  }

  MessageConfiguration() {
  }

  public void setMessages(Map<String, String> messages) {
    this.messages = messages;
  }

  public Map<String, String> getMessages() {
    return messages;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }
}
