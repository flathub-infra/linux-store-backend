package org.flathub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Created by jorge on 01/01/19.
 */
@Entity
public class AppRelease {

  private int appReleaseId;
  private Arch arch;
  private String appdataReleaseVersion;
  private boolean appdataReleaseVersionUpdated;
  private String ostreeCommitHash;
  private String ostreeCommitHashParent;
  private OffsetDateTime ostreeCommitDate;
  private OffsetDateTime ostreeCommitDateNext;
  private String ostreeCommitSubject;
  private int downloads;
  private int updates;
  private int installs;
  private App app;
  private String downloadSize;
  private String installedSize;
  private String runtime;
  private String sdk;
  private boolean isEndOfLife;
  private String endOfLifeInfo;
  private String endOfLifeRebase;
  private String metadata;


  @JsonIgnore
  @Id
  @SequenceGenerator(name = "apprelease_apprelease_id_seq",
    sequenceName = "apprelease_apprelease_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "apprelease_apprelease_id_seq")
  @Column(name = "apprelease_id", nullable = false)
  public int getAppReleaseId() {
    return appReleaseId;
  }

  public void setAppReleaseId(int appReleaseId) {
    this.appReleaseId = appReleaseId;
  }

  @Enumerated(EnumType.STRING)
  @Column(length = 32)
  @JsonIgnore
  public Arch getArch() {
    return arch;
  }

  public void setArch(Arch arch) {
    this.arch = arch;
  }

  public String getAppdataReleaseVersion() {
    return appdataReleaseVersion;
  }

  public void setAppdataReleaseVersion(String appdataReleaseVersion) {
    this.appdataReleaseVersion = appdataReleaseVersion;
  }

  public boolean isAppdataReleaseVersionUpdated() {
    return appdataReleaseVersionUpdated;
  }

  public void setAppdataReleaseVersionUpdated(boolean appdataReleaseVersionUpdated) {
    this.appdataReleaseVersionUpdated = appdataReleaseVersionUpdated;
  }

  public String getOstreeCommitHash() {
    return ostreeCommitHash;
  }

  @Transient
  public String getOstreeCommitShortHash() {

    if(ostreeCommitHash != null && ostreeCommitHash.length() > 12){
      return ostreeCommitHash.substring(0,12);
    }
    else{
      return null;
    }
  }

  public void setOstreeCommitHash(String ostreeCommitHash) {
    this.ostreeCommitHash = ostreeCommitHash;
  }

  public String getOstreeCommitHashParent() {
    return ostreeCommitHashParent;
  }

  public void setOstreeCommitHashParent(String ostreeCommitHashParent) {
    this.ostreeCommitHashParent = ostreeCommitHashParent;
  }

  @Transient
  public String getOstreeCommitShortHashParent() {

    if(ostreeCommitHashParent != null && ostreeCommitHashParent.length() > 12){
      return ostreeCommitHashParent.substring(0,12);
    }
    else{
      return null;
    }
  }

  public OffsetDateTime getOstreeCommitDate() {
    return ostreeCommitDate;
  }

  public void setOstreeCommitDate(OffsetDateTime ostreeCommitDate) {
    this.ostreeCommitDate = ostreeCommitDate;
  }

  public OffsetDateTime getOstreeCommitDateNext() {
    return ostreeCommitDateNext;
  }

  public void setOstreeCommitDateNext(OffsetDateTime ostreeCommitDateNext) {
    this.ostreeCommitDateNext = ostreeCommitDateNext;
  }

  public String getOstreeCommitSubject() {
    return ostreeCommitSubject;
  }

  public void setOstreeCommitSubject(String ostreeCommitSubject) {
    this.ostreeCommitSubject = ostreeCommitSubject;
  }

  public int getDownloads() {
    return downloads;
  }

  public void setDownloads(int downloads) {
    this.downloads = downloads;
  }

  public int getUpdates() {
    return updates;
  }

  public void setUpdates(int updates) {
    this.updates = updates;
  }

  public int getInstalls() {
    return installs;
  }

  public void setInstalls(int installs) {
    this.installs = installs;
  }

  public String getDownloadSize() {
    return downloadSize;
  }

  public void setDownloadSize(String downloadSize) {
    this.downloadSize = downloadSize;
  }

  public String getInstalledSize() {
    return installedSize;
  }

  public void setInstalledSize(String installedSize) {
    this.installedSize = installedSize;
  }

  public String getRuntime() {
    return runtime;
  }

  public void setRuntime(String runtime) {
    this.runtime = runtime;
  }

  public String getSdk() {
    return sdk;
  }

  public void setSdk(String sdk) {
    this.sdk = sdk;
  }


  @Column(name = "is_end_of_life")
  public boolean isEndOfLife() {
    return isEndOfLife;
  }

  public void setEndOfLife(boolean endOfLife) {
    isEndOfLife = endOfLife;
  }

  public String getEndOfLifeInfo() {
    return endOfLifeInfo;
  }

  public void setEndOfLifeInfo(String endOfLifeInfo) {
    this.endOfLifeInfo = endOfLifeInfo;
  }

  public String getEndOfLifeRebase() {
    return endOfLifeRebase;
  }

  public void setEndOfLifeRebase(String endOfLifeRebase) {
    this.endOfLifeRebase = endOfLifeRebase;
  }

  public String getMetadata() {
    return metadata;
  }

  public void setMetadata(String metadata) {
    this.metadata = metadata;
  }

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "app_id", referencedColumnName = "app_id")
  public App  getApp() {
    return this.app;
  }

  @JsonProperty
  public void setApp(App app) {
    this.app = app;
  }

  public AppRelease() {
  }

}
