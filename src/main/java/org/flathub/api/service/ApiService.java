package org.flathub.api.service;

import org.flathub.api.model.App;

import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
public interface ApiService {
    List<App> findAllApps();
    void addApp(App app);
}
