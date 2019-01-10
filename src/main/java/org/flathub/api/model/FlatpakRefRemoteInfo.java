package org.flathub.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FlatpakRefRemoteInfo {
  private String ref;
  private String id;
  private String arch;
  private String branch;
  private String collection;
  private LocalDateTime date;
  private String subject;
  private String commit;
  private String shortCommit;
  private String parent;
  private String downloadSize;
  private String installedSize;
  private String runtime;
  private String sdk;
  private boolean isEndOfLife;
  private String endOfLife;
  private String endOfLifeRebase;
  private String metadata;
  private ArrayList<FlatpakRefRemoteInfo> history;

  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getArch() {
    return arch;
  }

  public void setArch(String arch) {
    this.arch = arch;
  }

  public String getBranch() {
    return branch;
  }

  public void setBranch(String branch) {
    this.branch = branch;
  }

  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getCommit() {
    return commit;
  }

  public void setCommit(String commit) {
    this.commit = commit;
    if(commit != null && commit.length() > 12){
      this.shortCommit = commit.substring(0,12);
    }
  }

  public String getShortCommit() {
    return shortCommit;
  }

  public void setShortCommit(String shortCommit) {
    this.shortCommit = shortCommit;
  }

  public String getParent() {
    return parent;
  }

  public void setParent(String parent) {
    this.parent = parent;
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

  public boolean isEndOfLife() {
    return isEndOfLife;
  }

  public void setEndOfLife(boolean endOfLife) {
    isEndOfLife = endOfLife;
  }

  public String getEndOfLife() {
    return endOfLife;
  }

  public void setEndOfLife(String endOfLife) {
    this.endOfLife = endOfLife;
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

  public ArrayList<FlatpakRefRemoteInfo> getHistory() {
    return history;
  }

  public void setHistory(ArrayList<FlatpakRefRemoteInfo> history) {
    this.history = history;
  }
}
