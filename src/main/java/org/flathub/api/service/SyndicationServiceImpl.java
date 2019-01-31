package org.flathub.api.service;


import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.flathub.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SyndicationServiceImpl implements SyndicationService {


  @Autowired
  private AppRepository appRepository;

  @Autowired
  private AppReleaseRepository appReleaseRepository;

  @Override
  public String getFeedByCollection(String collectionName) throws FeedException {

    List<App> apps;

    if (ApiServiceImpl.COLLECTION_NAME_NEW.equalsIgnoreCase(collectionName)) {

      Sort.Order order = new Sort.Order(Sort.Direction.DESC, "InStoreSinceDate").nullsLast();
      apps = appRepository.findAllByInStoreSinceDateAfter(OffsetDateTime.now().
        minusDays(ApiServiceImpl.COLLECTION_NAME_NEW_DAYSBACK), new Sort(order));

      return createFeed("Flathub - New apps",
        "New applications published in Flathub in the last " + ApiServiceImpl.COLLECTION_NAME_NEW_DAYSBACK + " days",
        "https://flathub.org",
        "https://flathub.org/assets/themes/flathub/flathub-logo.png",
        apps, true);

    }
    else if(ApiServiceImpl.COLLECTION_NAME_RECENTLY_UPDATED.equalsIgnoreCase(collectionName)) {

      apps = appRepository.findRecentlyAddedOrUpdatedUsingAppReleaseX8664();

      if(apps == null && apps.size() == 0){
        apps = appRepository.findRecentlyAddedOrUpdated();
      }

      return createFeed("Flathub - Updated apps",
        "Applications updated in Flathub in the last " + ApiServiceImpl.COLLECTION_NAME_NEW_DAYSBACK + " days",
        "https://flathub.org",
        "https://flathub.org/assets/themes/flathub/flathub-logo.png",
        apps, true);
    }

    return "";

  }


  public String createFeed(String title, String feedDescription, String link, String iconUrl,
                           List<App> apps, boolean showReleases) throws FeedException {

    SyndFeed feed = new SyndFeedImpl();
    feed.setFeedType("atom_1.0");
    feed.setTitle(title);
    feed.setLink(link);
    feed.setDescription(feedDescription);

    SyndImage image = new SyndImageImpl();
    image.setUrl(iconUrl);
    feed.setIcon(image);

    List<SyndCategory> categories = new ArrayList<>();
    SyndCategory categoryLinux = new SyndCategoryImpl();
    categoryLinux.setName("Linux");
    categories.add(categoryLinux);

    List<SyndEntry> entries = new ArrayList<>();
    SyndEntry entry;
    AppRelease appRelease = null;
    String descriptionContents;

    for(App app: apps){

      entry = new SyndEntryImpl();
      entry.setTitle(app.getName());
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

      if(showReleases){

        appRelease = appReleaseRepository.findFirstByAppAndArchOrderByOstreeCommitDateDesc(app, Arch.X86_64);

        if(appRelease != null){
          descriptionContents = descriptionContents + "<br><h3>Additional information:</h3>";
          descriptionContents = descriptionContents + "<ul>";

          if(app.getCurrentReleaseVersion() != null && app.getCurrentReleaseVersion().length() > 0){
            descriptionContents = descriptionContents + "<li>Version: " + app.getCurrentReleaseVersion() + "</li>";
          }

          if(app.getDeveloperName() != null && app.getDeveloperName().length() > 0){
            descriptionContents = descriptionContents + "<li>Developer: " + app.getDeveloperName() + "</li>";
          }

          if(app.getProjectLicense() != null && app.getProjectLicense().length() > 0){

            if(app.getProjectLicense().contains("LicenseRef-proprietary")){
              descriptionContents = descriptionContents + "<li>License: Proprietary</li>";
            }
            else{
              descriptionContents = descriptionContents + "<li>License: " + app.getProjectLicense() + "</li>";
            }

          }

          descriptionContents = descriptionContents + "<li>Ostree Commit: <ul>";
          descriptionContents = descriptionContents + "<li>Subject: " + appRelease.getOstreeCommitSubject() + "</li>";
          descriptionContents = descriptionContents + "<li>Date: " + appRelease.getOstreeCommitDate() + "</li>";
          descriptionContents = descriptionContents + "<li>Commit: " + appRelease.getOstreeCommitHash() + "</li>";
          descriptionContents = descriptionContents + "</ul></li></ul>";
        }
      }


      if(showReleases && appRelease != null){
        entry.setUpdatedDate(Date.from(appRelease.getOstreeCommitDate().toInstant()));
      }
      else{
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
