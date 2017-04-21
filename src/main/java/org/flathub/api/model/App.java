package org.flathub.api.model;

import javax.persistence.*;

/**
 * Created by jorge on 24/03/17.
 */
@Entity
@Table(name = "APP", schema = "public")
public class App {
    private int appid;
    private String name;
    private String summary;
    private String description;
    private String projectLicense;
    private String homepageUrl;
    private String bugtrackerUrl;
    private String currentRelease;

    public App() {
    }

    public App(int appid, String name) {
        this.appid = appid;
        this.name = name;
    }

    @Id
    @SequenceGenerator(name="apps_appid_seq",
            sequenceName="apps_appid_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="apps_appid_seq")
    @Column(name = "appid", updatable=false)

    public int getAppid() {
        return appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        App app = (App) o;

        if (appid != app.appid) return false;
        if (name != null ? !name.equals(app.name) : app.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = appid;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "summary", nullable = true, length = 1024)
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "description", nullable = true, length = 4096)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "project_license", nullable = true, length = 1024)
    public String getProjectLicense() {
        return projectLicense;
    }

    public void setProjectLicense(String projectLicense) {
        this.projectLicense = projectLicense;
    }

    @Basic
    @Column(name = "homepage_url", nullable = true, length = 2048)
    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    @Basic
    @Column(name = "bugtracker_url", nullable = true, length = 2048)
    public String getBugtrackerUrl() {
        return bugtrackerUrl;
    }

    public void setBugtrackerUrl(String bugtrackerUrl) {
        this.bugtrackerUrl = bugtrackerUrl;
    }

    @Basic
    @Column(name = "current_release", nullable = true, length = 1024)
    public String getCurrentRelease() {
        return currentRelease;
    }

    public void setCurrentRelease(String currentRelease) {
        this.currentRelease = currentRelease;
    }
}
