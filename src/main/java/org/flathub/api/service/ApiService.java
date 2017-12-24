package org.flathub.api.service;

import java.util.List;
import org.flathub.api.model.App;
import org.flathub.api.model.Category;
import org.flathub.api.model.FlatpakRepo;
import org.flathub.api.model.Screenshot;

/**
 * Created by jorge on 24/03/17.
 */
public interface ApiService {


  void updateCategory(Category category);
  Category findCategoryByName(String categoryName);

  void updateApp(App app);

  List<App> findAllApps();

  List<App> findAllAppsByCategoryName(String categoryName);

  App findAppByFlatpakAppId(String flatpakAppId);

  FlatpakRepo findRepoByName(String name);

  void updateFlatpakRepo(FlatpakRepo repo);

  void updateScreenshot(Screenshot screenshot);

  void deleteScrenshotsByApp(App app);
}
