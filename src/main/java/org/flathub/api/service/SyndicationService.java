package org.flathub.api.service;

import com.rometools.rome.io.FeedException;
import org.flathub.api.model.App;
import org.flathub.api.model.FeedPublishBy;

import java.util.List;

public interface SyndicationService {

  String createFeed(String feedTitle, String feedDescription, String feedHomePageUrl, String feedIconUrl,
                           String feedCategory, List<App> apps, FeedPublishBy publishBy) throws FeedException;

}
