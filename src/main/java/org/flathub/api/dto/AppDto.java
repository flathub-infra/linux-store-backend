package org.flathub.api.dto;

/**
 * Created by jorge on 18/12/17.
 */
public class AppDto {

  private String flatpakAppId;
  private String name;
  private String summary;
  private String iconDesktopUrl;
  private String iconMobileUrl;
  private double rating;
  private int ratingVotes;

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

  public String getIconDesktopUrl() {
    return iconDesktopUrl;
  }

  public void setIconDesktopUrl(String iconDesktopUrl) {
    this.iconDesktopUrl = iconDesktopUrl;
  }

  public String getIconMobileUrl() {
    return iconMobileUrl;
  }

  public void setIconMobileUrl(String iconMobileUrl) {
    this.iconMobileUrl = iconMobileUrl;
  }

  public double getRating() {
    return rating;
  }

  public void setRating(double rating) {
    this.rating = rating;
  }

  public int getRatingVotes() {
    return ratingVotes;
  }

  public void setRatingVotes(int ratingVotes) {
    this.ratingVotes = ratingVotes;
  }
}
