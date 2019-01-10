package org.flathub.api.controller;

import com.rometools.rome.io.FeedException;
import org.flathub.api.dto.AppDto;
import org.flathub.api.dto.AppFullDto;
import org.flathub.api.dto.AppMapper;
import org.flathub.api.service.ApiService;
import org.flathub.api.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by jorge on 24/03/17.
 */
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:80", "http://45.55.104.129:80"})
@RestController
@RequestMapping("/api/v1")
public class ApiController {

  @Autowired
  private ApiService apiService;

  @Autowired
  private UpdateService updateService;

  @Autowired
  private AppMapper mapper;

  @RequestMapping(value = "/apps", method = RequestMethod.GET)
  public List<AppDto> findAll() {

    return mapper.appsToAppDtos(apiService.findAllApps());
  }

  @RequestMapping(value = "/apps/category/{categoryName}", method = RequestMethod.GET)
  public List<AppDto> findAllByCategory(@PathVariable String categoryName) {
    return mapper.appsToAppDtos(apiService.findAllAppsByCategoryName(categoryName));
  }

  @RequestMapping(value = "/apps/collection/{collectionName}", method = RequestMethod.GET)
  public List<AppDto> findAllByCollection(@PathVariable String collectionName) {
    return mapper.appsToAppDtos(apiService.findAllAppsByCollectionName(collectionName));
  }

  @RequestMapping(value = "/apps/collection/{collectionName}/feed", method = RequestMethod.GET)
  @ResponseBody public String getRssFeedByCollection(HttpServletResponse response, @PathVariable String collectionName) {
    try {
      response.setContentType("application/xml");
      return apiService.getRssFeedByCollectionName(collectionName);
    } catch (FeedException e) {
      e.printStackTrace();
      return "";
    }
  }


  @RequestMapping(value = "/apps/{flatpakAppId:.+}", method = RequestMethod.GET)
  public AppFullDto findAppFlatpakAppId(@PathVariable String flatpakAppId) {

    return mapper.appToAppFullDto(apiService.findAppByFlatpakAppId(flatpakAppId));
  }


}
