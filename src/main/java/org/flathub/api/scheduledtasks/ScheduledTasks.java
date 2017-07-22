package org.flathub.api.scheduledtasks;

import org.flathub.api.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Profile("SCHED")
public class ScheduledTasks {

  private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

  @Autowired
  private UpdateService updateService;


  @Scheduled(cron = "0 30 */2 * * *")
  public void updateFlathubInfo() {

    logger.info("Updating Flathub info");
    updateService.updateFlathubInfo();
  }

}
