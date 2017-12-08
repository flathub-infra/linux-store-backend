package org.flathub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jorge on 04/05/17.
 */

@Entity
public class App {

  //FIXME: read value from application-XXX.yml file
  //@Value("${flathub.icons.url}")
  @SuppressWarnings("FieldCanBeLocal")
  //private final String flathubIconsUrl = "http://localhost/main-store/icons";
  private final String flathubIconsUrl = "/main-store/icons";

  //FIXME: read value from application-XXX.yml file
  //@Value("${flathub.flatpakref.url}")
  @SuppressWarnings("FieldCanBeLocal")
  //private final String flathubFlatpakRefUrl = "http://localhost/main-store/apps";
  private final String flathubFlatpakRefUrl = "/main-store/apps";

  private int appId;
  private String flatpakAppId;
  private String name;
  private String summary;
  private String description;
  private String projectLicense;
  private String homepageUrl;
  private String bugtrackerUrl;
  private String currentRelease;
  private FlatpakRepo flatpakRepo;
  private Set<Category> categories = new HashSet<>();

  @JsonIgnore
  @Id
  @SequenceGenerator(name = "app_app_id_seq",
    sequenceName = "app_app_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "app_app_id_seq")
  @Column(name = "app_id", nullable = false)
  public int getAppId() {
    return appId;
  }

  @JsonProperty
  public void setAppId(int appId) {
    this.appId = appId;
  }

  @Basic
  @Column(name = "flatpak_app_id", nullable = false, length = 128)
  public String getFlatpakAppId() {
    return flatpakAppId;
  }

  public void setFlatpakAppId(String flatpakAppId) {
    this.flatpakAppId = flatpakAppId;
  }

  @Basic
  @Column(name = "name", nullable = false, length = 128)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "summary", length = 1024)
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  @Basic
  @Column(name = "description", length = 4096)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "project_license", length = 1024)
  public String getProjectLicense() {
    return projectLicense;
  }

  public void setProjectLicense(String projectLicense) {
    this.projectLicense = projectLicense;
  }

  @Basic
  @Column(name = "homepage_url", length = 2048)
  public String getHomepageUrl() {
    return homepageUrl;
  }

  public void setHomepageUrl(String homepageUrl) {
    this.homepageUrl = homepageUrl;
  }

  @Basic
  @Column(name = "bugtracker_url", length = 2048)
  public String getBugtrackerUrl() {
    return bugtrackerUrl;
  }

  public void setBugtrackerUrl(String bugtrackerUrl) {
    this.bugtrackerUrl = bugtrackerUrl;
  }

  @Basic
  @Column(name = "current_release", length = 1024)
  public String getCurrentRelease() {
    return currentRelease;
  }

  public void setCurrentRelease(String currentRelease) {
    this.currentRelease = currentRelease;
  }

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "flatpak_repo_id", referencedColumnName = "flatpak_repo_id")
  public FlatpakRepo getFlatpakRepo() {
    return this.flatpakRepo;
  }

  @JsonProperty
  public void setFlatpakRepo(FlatpakRepo repo) {
    this.flatpakRepo = repo;
    if (!flatpakRepo.getApps().contains(
      this)) {// warning this may cause performance issues if you have a large data set since this operation is O(n)
      flatpakRepo.getApps().add(this);

    }
  }

  @JsonInclude()
  @Transient
  public String getIconUrl() {
    return flathubIconsUrl + "/" + this.getFlatpakAppId() + ".png";
  }

  @JsonInclude()
  @Transient
  public String getDownloadFlatpakRefUrl() {
    return flathubFlatpakRefUrl + "/" + this.getFlatpakAppId() + ".flatpakref";
  }

  @ManyToMany(cascade = {
    CascadeType.PERSIST,
    CascadeType.MERGE
  })
  @JoinTable(name = "app_category",
    joinColumns = @JoinColumn(name = "app_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  public Set<Category> getCategories() {
    return categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  public void addCategory(Category category) {
    categories.add(category);
    if (!category.getApps().contains(
      this)) {// warning this may cause performance issues if you have a large data set since this operation is O(n)
      category.getApps().add(this);
    }
  }

  public void removeCategory(Category category) {
    categories.remove(category);
    category.getApps().remove(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof App)) return false;

    App app = (App) o;

    if (appId != app.appId) return false;
    if (flathubIconsUrl != null ? !flathubIconsUrl.equals(app.flathubIconsUrl) : app.flathubIconsUrl != null)
      return false;
    if (flathubFlatpakRefUrl != null ? !flathubFlatpakRefUrl.equals(app.flathubFlatpakRefUrl) : app.flathubFlatpakRefUrl != null)
      return false;
    if (!flatpakAppId.equals(app.flatpakAppId)) return false;
    if (!name.equals(app.name)) return false;
    if (summary != null ? !summary.equals(app.summary) : app.summary != null) return false;
    if (description != null ? !description.equals(app.description) : app.description != null) return false;
    if (projectLicense != null ? !projectLicense.equals(app.projectLicense) : app.projectLicense != null) return false;
    if (homepageUrl != null ? !homepageUrl.equals(app.homepageUrl) : app.homepageUrl != null) return false;
    if (bugtrackerUrl != null ? !bugtrackerUrl.equals(app.bugtrackerUrl) : app.bugtrackerUrl != null) return false;
    if (currentRelease != null ? !currentRelease.equals(app.currentRelease) : app.currentRelease != null) return false;
    return flatpakRepo != null ? flatpakRepo.equals(app.flatpakRepo) : app.flatpakRepo == null;
  }

  @Override
  public int hashCode() {
    int result = flathubIconsUrl != null ? flathubIconsUrl.hashCode() : 0;
    result = 31 * result + (flathubFlatpakRefUrl != null ? flathubFlatpakRefUrl.hashCode() : 0);
    result = 31 * result + appId;
    result = 31 * result + flatpakAppId.hashCode();
    result = 31 * result + name.hashCode();
    result = 31 * result + (summary != null ? summary.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (projectLicense != null ? projectLicense.hashCode() : 0);
    result = 31 * result + (homepageUrl != null ? homepageUrl.hashCode() : 0);
    result = 31 * result + (bugtrackerUrl != null ? bugtrackerUrl.hashCode() : 0);
    result = 31 * result + (currentRelease != null ? currentRelease.hashCode() : 0);
    result = 31 * result + (flatpakRepo != null ? flatpakRepo.hashCode() : 0);
    return result;
  }
}
