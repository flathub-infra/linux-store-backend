package org.flathub.api.service;

import java.util.List;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.io.FeedException;
import org.flathub.api.model.*;

/**
 * Created by jorge on 24/03/17.
 */
public interface ApiService {


  /** Categories */
  void updateCategory(Category category);

  Category findCategoryByName(String categoryName);


  /** Apps */
  void updateApp(App app);

  List<App> findAllApps();

  List<App> findAllAppsByCategoryName(String categoryName);

  List<App> findAllAppsByCollectionName(String collectionName);

  App findAppByFlatpakAppId(String flatpakAppId);


  /** App Releases */
  void updateAppRelease(AppRelease appRelease);

  List<AppRelease> findAppReleaseByAppAndArch(App app, Arch arch);

  AppRelease findLastAppReleaseByAppAndArch(App app, Arch x8664);

  AppRelease findOneAppReleaseByAppAndArchAndOstreeCommitHash(App app, Arch arch, String commit);


  /** Repos */
  void updateFlatpakRepo(FlatpakRepo repo);

  FlatpakRepo findRepoByName(String name);


  /** Screenshots */
  void updateScreenshot(Screenshot screenshot);

  void deleteScrenshotsByApp(App app);

  /** RSS Feeds */
  String getRssFeedByCollectionName(String collectionName) throws FeedException;
}
