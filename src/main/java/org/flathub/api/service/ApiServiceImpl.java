package org.flathub.api.service;

import com.rometools.rome.io.FeedException;
import org.flathub.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
@Service
public class ApiServiceImpl implements ApiService {

  public static final String COLLECTION_NAME_RECENTLY_UPDATED = "recently-updated";
  public static final String COLLECTION_NAME_NEW = "new";
  public static final int COLLECTION_NAME_NEW_DAYSBACK = 30;

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

  @Autowired
  private SyndicationService syndicationService;

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

      //return appRepository.findRecentlyAddedOrUpdated();
      return appRepository.findRecentlyAddedOrUpdatedUsingAppReleaseX8664();

    } else if (COLLECTION_NAME_NEW.equalsIgnoreCase(collectionName)) {

      Sort.Order order = new Sort.Order(Direction.DESC, "InStoreSinceDate").nullsLast();
      return appRepository.findAllByInStoreSinceDateAfter(OffsetDateTime.now().minusDays(COLLECTION_NAME_NEW_DAYSBACK), new Sort  (order));

    } else {
      return new ArrayList<>();
    }
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

    List<App> apps = this.findAllAppsByCollectionName(collectionName);

    if (COLLECTION_NAME_RECENTLY_UPDATED.equalsIgnoreCase(collectionName)) {

      return syndicationService.createFeed("Flathub - Updated apps",
        "Applications updated in Flathub in the last 7 days",
        "https://flathub.org/apps/collection/recently-updated",
        "https://flathub.org/assets/themes/flathub/flathub-logo.png",
        "Linux",
        apps,  FeedPublishBy.AppLastChange);


    } else if (COLLECTION_NAME_NEW.equalsIgnoreCase(collectionName)) {

      return syndicationService.createFeed("Flathub - New apps",
        "New applications published in Flathub in the last " + ApiServiceImpl.COLLECTION_NAME_NEW_DAYSBACK + " days",
        "https://flathub.org/apps/collection/new",
        "https://flathub.org/assets/themes/flathub/flathub-logo.png",
        "Linux",
        apps,  FeedPublishBy.AppInStoreSince);

    }

    return  "";


  }

}
