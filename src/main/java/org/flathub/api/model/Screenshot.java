package org.flathub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

/**
 * Created by jorge on 08/12/17.
 */
@Entity
public class Screenshot {

  private int screenshotId;
  private String thumbUrl;
  private String imgMobileUrl;
  private String imgDesktopUrl;
  private App app;

  @JsonIgnore
  @Id
  @SequenceGenerator(name = "screenshot_screenshot_id_seq",
    sequenceName = "screenshot_screenshot_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "screenshot_screenshot_id_seq")
  @Column(name = "screenshot_id", nullable = false)
  public int getScreenshotId() {
    return screenshotId;
  }

  @JsonProperty
  public void setScreenshotId(int screenshotId) {
    this.screenshotId = screenshotId;
  }

  @Basic
  @Column(name = "thumb_url", length = 2048)
  public String getThumbUrl() {
    return thumbUrl;
  }

  public void setThumbUrl(String thumbUrl) {
    this.thumbUrl = thumbUrl;
  }

  @Basic
  @Column(name = "img_mobile_url", length = 2048)
  public String getImgMobileUrl() {
    return imgMobileUrl;
  }

  public void setImgMobileUrl(String imgMobileUrl) {
    this.imgMobileUrl = imgMobileUrl;
  }

  @Basic
  @Column(name = "img_desktop_url", length = 2048)
  public String getImgDesktopUrl() {
    return imgDesktopUrl;
  }

  public void setImgDesktopUrl(String imgDesktopUrl) {
    this.imgDesktopUrl = imgDesktopUrl;
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

  public Screenshot() {
  }

}
