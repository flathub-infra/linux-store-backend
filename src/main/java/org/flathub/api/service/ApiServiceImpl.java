package org.flathub.api.service;

import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;
import org.flathub.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
@Service
public class ApiServiceImpl implements ApiService {

  private static final String COLLECTION_NAME_RECENTLY_UPDATED = "recently-updated";
  private static final String COLLECTION_NAME_NEW = "new";

  @Autowired
  private AppRepository appRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ScreenshotRepository screenshotRepository;

  @Autowired
  private FlatpakRepoRepository repoRepository;

  @Autowired
  private AppReleaseRepository appReleaseRepository;


  @Override
  public List<App> findAllApps() {
    Sort.Order order = new Sort.Order(Direction.ASC, "name").ignoreCase();
    return appRepository.findAll(new Sort(order));
  }

  @Override
  public List<App> findAllAppsByCategoryName(String categoryName) {
    Sort.Order order = new Sort.Order(Direction.ASC, "name").ignoreCase();
    return appRepository.findByCategories_Name(categoryName, new Sort(order));
  }

  @Override
  public List<App> findAllAppsByCollectionName(String collectionName) {

    if (COLLECTION_NAME_RECENTLY_UPDATED.equalsIgnoreCase(collectionName)) {
     return this.findRecentlyUpdatedApps();
    } else {
      return new ArrayList<>();
    }
  }

  private List<App> findRecentlyUpdatedApps(){
    return appRepository.findRecentlyAddedOrUpdated();
  }

  @Override
  public App findAppByFlatpakAppId(String flatpakAppId) {
    return appRepository.findOneByFlatpakAppId(flatpakAppId);
  }

  @Override
  public List<AppRelease> findAppReleaseByAppAndArch(App app, Arch arch) {

    return appReleaseRepository.findByAppAndArch(app, arch);
  }

  @Override
  public void updateCategory(Category category) {
    categoryRepository.save(category);
  }

  @Override
  public Category findCategoryByName(String categoryName) {
    return categoryRepository.findOneByName(categoryName);
  }



  @Override
  public void updateApp(App app) {
    appRepository.save(app);
  }

  @Override
  public void updateFlatpakRepo(FlatpakRepo repo) {
    repoRepository.save(repo);
  }

  @Override
  public void updateScreenshot(Screenshot screenshot) {
    screenshotRepository.save(screenshot);
  }

  @Override
  public void deleteScrenshotsByApp(App app) {
    screenshotRepository.deleteScrenshotsByApp(app);
  }

  @Override
  public void updateAppRelease(AppRelease appRelease) {
    appReleaseRepository.save(appRelease);
  }

  @Override
  public AppRelease findLastAppReleaseByAppAndArch(App app, Arch arch) {
    return appReleaseRepository.findFirstByAppAndArchOrderByOstreeCommitDateDesc(app,arch);
  }

  @Override
  public AppRelease findOneAppReleaseByAppAndArchAndOstreeCommitHash(App app, Arch arch, String commit) {
    return appReleaseRepository.findOneAppReleaseByAppAndArchAndOstreeCommitHash(app,arch,commit);
  }

  @Override
  public FlatpakRepo findRepoByName(String name) {
    return repoRepository.findOneByName(name);
  }


  @Override
  public String getRssFeedByCollectionName(String collectionName) throws FeedException {

    SyndFeed feed = new SyndFeedImpl();
    feed.setFeedType("atom_1.0");
    feed.setTitle("Flathub - New apps");
    feed.setLink("https://flathub.org");
    feed.setDescription("New applications published in Flathub");

    SyndImage image = new SyndImageImpl();
    image.setUrl("https://flathub.org/assets/themes/flathub/flathub-logo.png");
    feed.setIcon(image);

    List<SyndCategory> categories = new ArrayList<>();
    SyndCategory categoryLinux = new SyndCategoryImpl();
    categoryLinux.setName("Linux");
    categories.add(categoryLinux);

    List<SyndEntry> entries = new ArrayList<>();


    if (COLLECTION_NAME_NEW.equalsIgnoreCase(collectionName)) {

      List<App> apps = appRepository.findRecentlyAdded();

      SyndEntry entry;
      String descriptionContents;


      for(App app: apps){

        entry = new SyndEntryImpl();
        entry.setTitle(app.getName());
        entry.setLink("https://flathub.org/apps/details/" + app.getFlatpakAppId());

        SyndContent description = new SyndContentImpl();
        description.setType("text/html");

        descriptionContents = app.getDescription();

        if(app.getCurrentReleaseVersion() != null){
          descriptionContents = descriptionContents + "<br><p>Version: " + app.getCurrentReleaseVersion() + "</p>";
        }

        if(app.getScreenshots() != null && app.getScreenshots().size()>0){
          descriptionContents = descriptionContents + "<br><img src=\"" + app.getScreenshots().get(0).getImgDesktopUrl() + "\">";
        }

        description.setValue(descriptionContents);
        entry.setDescription(description);

        entry.setCategories(categories);
        entry.setUpdatedDate(Date.from(app.getInStoreSinceDate().toInstant()));

        entries.add(entry);
      }

    }

    feed.setEntries(entries);

    return  new SyndFeedOutput().outputString(feed);
  }

}
