package org.flathub.api.controller;

import java.util.List;
import org.flathub.api.model.App;
import org.flathub.api.service.ApiService;
import org.flathub.api.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jorge on 24/03/17.
 */
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1")
class ApiController {

  @Autowired
  private ApiService apiService;

  @Autowired
  private UpdateService updateService;

  @RequestMapping(value = "/apps", method = RequestMethod.GET)
  public List<App> findAll() {
    return apiService.findAllApps();
  }

  @RequestMapping(value = "/apps/update", method = RequestMethod.GET)
  public void updateRepoInfo() {
    updateService.updateFlathubInfo();
  }

}
