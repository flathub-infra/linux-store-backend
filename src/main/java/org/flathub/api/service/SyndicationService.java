package org.flathub.api.service;

import com.rometools.rome.io.FeedException;

public interface SyndicationService {

  String getFeedByCollection(String collectionName) throws FeedException;

}
