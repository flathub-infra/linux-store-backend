package org.flathub.api.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jorge on 10/12/17.
 */
@Repository
public interface ScreenshotRepository extends JpaRepository<Screenshot, Integer> {

  List<Screenshot> findAll();
  void deleteScrenshotsByApp(App app);
}
