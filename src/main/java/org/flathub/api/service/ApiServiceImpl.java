package org.flathub.api.service;

import org.flathub.api.model.App;
import org.flathub.api.model.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
@Service
public class ApiServiceImpl implements ApiService{

    @Autowired
    private AppRepository repository;


    @Override
    public List<App> findAllApps() {
        return repository.findAll();
    }

    @Override
    public void addApp(App app) {
        repository.save(app);
    }
}
