package org.flathub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
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

  private final String flathubIconsUrl = "/repo/appstream/x86_64/icons/128x128";
  private final String flathubFlatpakRefUrl = "/repo/appstream";

  private int appId;
  private String flatpakAppId;
  private String name;
  private String summary;
  private String description;
  private String projectLicense;
  private String homepageUrl;
  private String bugtrackerUrl;
  private String currentReleaseVersion;
  private OffsetDateTime currentReleaseDate;
  private String firstReleaseVersion;
  private OffsetDateTime firstReleaseDate;
  private FlatpakRepo flatpakRepo;
  private double rating;
  private int ratingVotes;
  private Set<Category> categories = new HashSet<>();
  private List<Screenshot> screenshots = new ArrayList<>();



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
  @Column(name = "current_release_version", length = 1024)
  public String getCurrentReleaseVersion() {
    return currentReleaseVersion;
  }

  public void setCurrentReleaseVersion(String currentReleaseVersion) {
    this.currentReleaseVersion = currentReleaseVersion;
  }

  @Column(name = "current_release_date")
  public OffsetDateTime getCurrentReleaseDate() {
    return currentReleaseDate;
  }

  public void setCurrentReleaseDate(OffsetDateTime currentReleaseDate) {
    this.currentReleaseDate = currentReleaseDate;
  }

  @Basic
  @Column(name = "first_release_version", length = 1024)
  public String getFirstReleaseVersion() {
    return firstReleaseVersion;
  }

  public void setFirstReleaseVersion(String firstReleaseVersion) {
    this.firstReleaseVersion = firstReleaseVersion;
  }

  @Column(name = "first_release_date")
  public OffsetDateTime getFirstReleaseDate() {

    return firstReleaseDate;
  }

  public void setFirstReleaseDate(OffsetDateTime firstReleaseDate) {
    this.firstReleaseDate = firstReleaseDate;
  }

  @Basic
  @Column(name = "rating")
  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  @Basic
  @Column(name = "rating_votes")
  public int getRatingVotes() {
    return ratingVotes;
  }

  public void setRatingVotes(int ratingVotes) {
    this.ratingVotes = ratingVotes;
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
  }, fetch = FetchType.LAZY)
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

    if(!this.categories.contains(category)) {
      categories.add(category);
    }

  }

  public void removeCategory(Category category) {
    categories.remove(category);
  }

  @OneToMany(mappedBy = "app",
    cascade = CascadeType.ALL,
    orphanRemoval = true,
    fetch = FetchType.LAZY)
  public List<Screenshot> getScreenshots() {
    return screenshots;
  }

  public void setScreenshots(List<Screenshot> screenshots) {
    this.screenshots = screenshots;
  }

  public void addScreenshot(Screenshot screenshot) {

    this.screenshots.add(screenshot);
    screenshot.setApp(this);

  }

  public void removeScreenshot(Screenshot screenshot) {

    this.screenshots.remove(screenshot);
    screenshot.setApp(null);
  }


}
