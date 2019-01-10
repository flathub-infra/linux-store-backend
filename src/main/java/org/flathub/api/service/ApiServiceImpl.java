package org.flathub.api.service;

import java.util.ArrayList;
import java.util.List;

import org.flathub.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * Created by jorge on 24/03/17.
 */
@Service
public class ApiServiceImpl implements ApiService {

  private static final String COLLECTION_NAME_RECENTLY_UPDATED = "recently-updated";

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
}
