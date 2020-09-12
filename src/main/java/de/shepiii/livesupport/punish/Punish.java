package de.shepiii.livesupport.punish;

import javax.persistence.*;

@Entity
@Table(name = "punishs")
public final class Punish {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(length = 36)
  private String punishedId;
  @Column(length = 36)
  private String whoPunishedId;
  private Long pronounced;
  private Long end;
  private boolean unPunished;
  @Column(length = 36)
  private String unPunishedId;

  public Punish(
    Long id,
    String punishedId,
    String whoPunishedId,
    Long pronounced,
    Long end,
    boolean unPunished,
    String unPunishedId
  ) {
    this.id = id;
    this.punishedId = punishedId;
    this.whoPunishedId = whoPunishedId;
    this.pronounced = pronounced;
    this.end = end;
    this.unPunished = unPunished;
    this.unPunishedId = unPunishedId;
  }

  public Punish(
    String punishedId,
    String whoPunishedId,
    Long pronounced,
    Long end,
    boolean unPunished,
    String unPunishedId
  ) {
    this.punishedId = punishedId;
    this.whoPunishedId = whoPunishedId;
    this.pronounced = pronounced;
    this.end = end;
    this.unPunished = unPunished;
    this.unPunishedId = unPunishedId;
  }

  public Punish(
    String punishedId,
    String whoPunishedId,
    Long end
  ) {
    this.punishedId = punishedId;
    this.whoPunishedId = whoPunishedId;
    this.pronounced = System.currentTimeMillis();
    this.end = end;
    this.unPunished = false;
    this.unPunishedId = "N/A";
  }

  public Punish() {
  }

  public void setPunishedId(String punishedId) {
    this.punishedId = punishedId;
  }

  public void setUnPunishedId(String unPunishedId) {
    this.unPunishedId = unPunishedId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setWhoPunishedId(String whoPunishedId) {
    this.whoPunishedId = whoPunishedId;
  }

  public void setUnPunished(boolean unPunished) {
    this.unPunished = unPunished;
  }

  public void setPronounced(Long pronounced) {
    this.pronounced = pronounced;
  }

  public void setEnd(Long end) {
    this.end = end;
  }

  public boolean isActive() {
    if (unPunished) {
      return false;
    }
    return end > System.currentTimeMillis();
  }

  public Long getId() {
    return id;
  }

  public String getPunishedId() {
    return punishedId;
  }

  public String getWhoPunishedId() {
    return whoPunishedId;
  }

  public Long getPronounced() {
    return pronounced;
  }

  public Long getEnd() {
    return end;
  }

  public boolean isUnPunished() {
    return unPunished;
  }

  public String getUnPunishedId() {
    return unPunishedId;
  }
}
