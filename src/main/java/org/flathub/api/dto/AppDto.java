package org.flathub.api.dto;

/**
 * Created by jorge on 18/12/17.
 */
public class AppDto {

  private String flatpakAppId;
  private String name;
  private String summary;
  private String iconUrl;
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

  public String getIconUrl() {
    return iconUrl;
  }

  public void setIconUrl(String iconUrl) {
    this.iconUrl = iconUrl;
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
