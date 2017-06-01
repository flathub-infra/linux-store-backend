package org.flathub.api.service;

import java.util.List;
import org.flathub.api.model.App;
import org.flathub.api.model.FlatpakRepo;

/**
 * Created by jorge on 24/03/17.
 */
public interface ApiService {


  void updateApp(App app);

  List<App> findAllApps();

  App findAppByFlatpakAppId(String flatpakAppId);

  FlatpakRepo findRepoByName(String name);

  void updateFlatpakRepo(FlatpakRepo repo);

}
