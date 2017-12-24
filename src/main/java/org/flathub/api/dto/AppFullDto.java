package org.flathub.api.dto;

import java.util.HashSet;
import java.util.Set;
import org.flathub.api.model.Category;
import org.flathub.api.model.Screenshot;

/**
 * Created by jorge on 19/12/17.
 */
public class AppFullDto {

  private String flatpakAppId;
  private String name;
  private String summary;
  private String description;
  private String projectLicense;
  private String homepageUrl;
  private String bugtrackerUrl;
  private String iconUrl;
  private String downloadFlatpakRefUrl;
  private String currentRelease;
  private Set<CategoryDto> categories = new HashSet<>();
  private Set<Screenshot> screenshots = new HashSet<>();

  public String getFlatpakAppId() {
    return flatpakAppId;
  }

  public void setFlatpakAppId(String flatpakAppId) {
    this.flatpakAppId = flatpakAppId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getProjectLicense() {
    return projectLicense;
  }

  public void setProjectLicense(String projectLicense) {
    this.projectLicense = projectLicense;
  }

  public String getHomepageUrl() {
    return homepageUrl;
  }

  public void setHomepageUrl(String homepageUrl) {
    this.homepageUrl = homepageUrl;
  }

  public String getBugtrackerUrl() {
    return bugtrackerUrl;
  }

  public void setBugtrackerUrl(String bugtrackerUrl) {
    this.bugtrackerUrl = bugtrackerUrl;
  }

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
  }

  public String getDownloadFlatpakRefUrl() {
    return downloadFlatpakRefUrl;
  }

  public void setDownloadFlatpakRefUrl(String downloadFlatpakRefUrl) {
    this.downloadFlatpakRefUrl = downloadFlatpakRefUrl;
  }

  public String getCurrentRelease() {
    return currentRelease;
  }

  public void setCurrentRelease(String currentRelease) {
    this.currentRelease = currentRelease;
  }

  public Set<CategoryDto> getCategories() {
    return categories;
  }

  public void setCategories(Set<CategoryDto> categories) {
    this.categories = categories;
  }

  public Set<Screenshot> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(Set<Screenshot> screenshots) {
    this.screenshots = screenshots;
  }

}
