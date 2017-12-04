package org.flathub.api.service;

import java.util.List;
import org.flathub.api.model.App;
import org.flathub.api.model.AppRepository;
import org.flathub.api.model.FlatpakRepo;
import org.flathub.api.model.FlatpakRepoRepository;
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
  private FlatpakRepoRepository repoRepository;


  @Override
  public List<App> findAllApps() {
    Sort.Order order = new Sort.Order(Direction.ASC, "name").ignoreCase();
    return appRepository.findAll(new Sort(order));
  }

  @Override
  public App findAppByFlatpakAppId(String flatpakAppId) {
    return appRepository.findOneByFlatpakAppId(flatpakAppId);
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
  public FlatpakRepo findRepoByName(String name) {
    return repoRepository.findOneByName(name);
  }
}
