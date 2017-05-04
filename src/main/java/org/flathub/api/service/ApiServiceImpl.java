package org.flathub.api.service;

import org.flathub.api.model.App;
import org.flathub.api.model.AppRepository;
import org.flathub.api.model.FlatpakRepo;
import org.flathub.api.model.FlatpakRepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
@Service
public class ApiServiceImpl implements ApiService{

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private FlatpakRepoRepository repoRepository;


    @Override
    public List<App> findAllApps() {
        return appRepository.findAll();
    }

    @Override
    public void addApp(App app) {
        appRepository.save(app);
    }

    @Override
    public void addFlatpakRepo(FlatpakRepo repo) {
        repoRepository.save(repo);
    }

    @Override
    public void updateFlatpakRepo(FlatpakRepo repo) {
        repoRepository.save(repo);
    }
}
