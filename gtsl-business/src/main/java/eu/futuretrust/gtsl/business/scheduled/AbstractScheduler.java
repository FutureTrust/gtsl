/*
 * Copyright (c) 2017 European Commission.
 *
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by the European
 * Commission - subsequent versions of the EUPL (the "Licence").
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl5
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the Licence
 * is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 * See the Licence for the specific language governing permissions and limitations under the
 * Licence.
 */

package eu.futuretrust.gtsl.business.scheduled;

import eu.futuretrust.gtsl.business.properties.helper.Cron;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

public abstract class AbstractScheduler implements Runnable {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractScheduler.class);

  protected ScheduledFuture scheduledFuture;
  protected TaskScheduler taskScheduler;

  @Override
  public abstract void run();

  @PostConstruct
  public abstract void initializeScheduler();

  public void reSchedule(Cron cron) {
    if (taskScheduler == null) {
      taskScheduler = new ConcurrentTaskScheduler();
    }
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
    }
    LOGGER.info("Task rescheduled [" + cron.getValue() + "," + cron.isEnabled() + "]");
    scheduledFuture = taskScheduler.schedule(this, new CronTrigger(cron.getValue()));
  }

}
