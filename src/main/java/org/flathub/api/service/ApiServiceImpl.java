package org.flathub.api.service;

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

  @Autowired
  private AppRepository appRepository;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ScreenshotRepository screenshotRepository;

  @Autowired
  private FlatpakRepoRepository repoRepository;


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
  public App findAppByFlatpakAppId(String flatpakAppId) {
    return appRepository.findOneByFlatpakAppId(flatpakAppId);
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
  public FlatpakRepo findRepoByName(String name) {
    return repoRepository.findOneByName(name);
  }
}
