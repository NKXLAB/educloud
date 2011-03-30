package com.google.educloud.cloudserver.scheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.configuration.ServerConfig;
import com.google.educloud.cloudserver.database.dao.TaskDao;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask;

public class Scheduler implements Runnable {

	private static Logger LOG = Logger.getLogger(Scheduler.class);

	private static final long INTERNAL = ServerConfig.getScheduleInterval();

	private ExecutorService es;

	private static int currentTaskIdFactory;

	public Scheduler() {
		es = Executors.newCachedThreadPool(new ScheduleThreadFactory());
	}

	@Override
	public void run() {
		while (true) {
			try {
				// will consume PENDING tasks
				List<CloudTask> searchForNewTasks = searchForNewTasks();

				for (CloudTask cloudTask : searchForNewTasks) {
					consume(cloudTask);
				}

				Thread.sleep(INTERNAL);
			} catch (InterruptedException e) {
				LOG.error("Scheduler was interrupted", e);
			}
		}
	}

	private List<CloudTask> searchForNewTasks() {
		return TaskDao.getInstance().findPendingTasks();
	}

	private void consume(CloudTask task) {
		LOG.debug("Scheduler will consume a new task");

		currentTaskIdFactory = task.getId();

		es.execute(task);
	}

	/**
     * The schedule thread factory
     */
    static class ScheduleThreadFactory implements ThreadFactory {
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);

        ScheduleThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null)? s.getThreadGroup() :
                                 Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, "task-" + currentTaskIdFactory + "-thread-" + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
