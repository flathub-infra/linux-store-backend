package org.flathub.api.scheduledtasks;

import org.flathub.api.service.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
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

  // Run at application start
  @EventListener(ContextRefreshedEvent.class)
  public void contextRefreshedEvent() {
      this.updateFlathubInfo();
  }

  @Scheduled(cron = "0 */10 * * * *")
  public void updateFlathubInfo() {

    updateService.updateFlathubInfo();

  }

}
