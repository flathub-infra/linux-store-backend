package org.flathub.api.service;


import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.flathub.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SyndicationServiceImpl implements SyndicationService {


  @Autowired
  private AppRepository appRepository;

  @Autowired
  private AppReleaseRepository appReleaseRepository;


  public String createFeed(String feedTitle, String feedDescription, String feedHomePageUrl, String feedIconUrl,
                           String feedCategory, List<App> apps, FeedPublishBy publishBy) throws FeedException {

    SyndFeed feed = new SyndFeedImpl();
    feed.setFeedType("rss_2.0");
    feed.setTitle(feedTitle);
    feed.setLink(feedHomePageUrl);
    feed.setDescription(feedDescription);

    SyndImage image = new SyndImageImpl();
    image.setUrl(feedIconUrl);
    feed.setIcon(image);

    List<SyndCategory> categories = new ArrayList<>();
    SyndCategory category = new SyndCategoryImpl();
    category.setName(feedCategory);
    categories.add(category);

    List<SyndEntry> entries = new ArrayList<>();
    SyndEntry entry;
    AppRelease appRelease;
    String descriptionContents;

    for(App app: apps){

      entry = new SyndEntryImpl();
      entry.setTitle(app.getName());

      //TODO: do not harcode this url here
      entry.setLink("https://flathub.org/apps/details/" + app.getFlatpakAppId());

      SyndContent description = new SyndContentImpl();
      description.setType("text/html");

      descriptionContents = "";

      if(app.getIconDesktopUrl() != null && app.getIconDesktopUrl().length() > 0){

        if(app.getIconDesktopUrl().startsWith("http")){
          descriptionContents = "<img style=\"width:64px;\" src=\"" + app.getIconDesktopUrl() + "\">";
        }
        else{
          descriptionContents = "<img style=\"width:64px;\" src=\"https://flathub.org/" + app.getIconDesktopUrl() + "\">";
        }

      }

      if(app.getSummary() != null && app.getSummary().length() > 0){
        descriptionContents = descriptionContents + "<p><i>" + app.getSummary() + "</i></p>";
      }

      if(app.getDescription() != null && app.getDescription().length() > 0){
        descriptionContents = descriptionContents + app.getDescription() + "<br>";
      }

      if(app.getScreenshots() != null && app.getScreenshots().size()>0){
        descriptionContents = descriptionContents + "<br><img src=\"" + app.getScreenshots().get(0).getImgDesktopUrl() + "\"><br>";
      }


      descriptionContents = descriptionContents + "<br><h3>Additional information:</h3>";
      descriptionContents = descriptionContents + "<ul>";

      if(app.getCurrentReleaseVersion() != null && app.getCurrentReleaseVersion().length() > 0){
        descriptionContents = descriptionContents + "<li>Version: " + app.getCurrentReleaseVersion() + "</li>";
      }
      else{
        descriptionContents = descriptionContents + "<li>Version: &mdash;\n </li>";
      }

      if(app.getDeveloperName() != null && app.getDeveloperName().length() > 0){
        descriptionContents = descriptionContents + "<li>Developer: " + app.getDeveloperName() + "</li>";
      }
      else{
        descriptionContents = descriptionContents + "<li>Developer: &mdash;\n </li>";
      }

      if(app.getProjectLicense() != null && app.getProjectLicense().length() > 0){
        if(app.getProjectLicense().contains("LicenseRef-proprietary")){
          descriptionContents = descriptionContents + "<li>License: Proprietary</li>";
        }
        else{
          descriptionContents = descriptionContents + "<li>License: " + app.getProjectLicense() + "</li>";
        }
      }
      else{
        descriptionContents = descriptionContents + "<li>License: &mdash;\n </li>";
      }

      // Close the "Additional info" section
      descriptionContents = descriptionContents + "</ul>";

      appRelease = appReleaseRepository.findFirstByAppAndArchOrderByOstreeCommitDateDesc(app, Arch.X86_64);
      if(appRelease != null){
        descriptionContents = descriptionContents + "<br><h3>Latest changes:</h3>";
        descriptionContents = descriptionContents + "<ul>";
        descriptionContents = descriptionContents + "<li>Subject: " + appRelease.getOstreeCommitSubject() + "</li>";
        descriptionContents = descriptionContents + "<li>Date: " + appRelease.getOstreeCommitDate() + "</li>";
        descriptionContents = descriptionContents + "<li>Hash: " + appRelease.getOstreeCommitShortHash() + "</li>";
        descriptionContents = descriptionContents + "</ul>";
      }

      if(publishBy == FeedPublishBy.AppLastChange){

        if(appRelease != null && appRelease.getOstreeCommitDate() != null){
          entry.setUpdatedDate(Date.from(appRelease.getOstreeCommitDate().toInstant()));
        }
        else if(app.getCurrentReleaseDate() != null){
          entry.setUpdatedDate(Date.from(app.getCurrentReleaseDate().toInstant()));
        }

      }
      else if(publishBy == FeedPublishBy.AppInStoreSince){
        entry.setUpdatedDate(Date.from(app.getInStoreSinceDate().toInstant()));
      }

      description.setValue(descriptionContents);
      entry.setDescription(description);

      entry.setCategories(categories);


      entries.add(entry);

    }

    feed.setEntries(entries);

    return  new SyndFeedOutput().outputString(feed);

  }


}
