package com.epam.esm.security.eventlistener;

import com.epam.esm.dao.security.RefreshTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshTokenEventListener {

    private final RefreshTokenDao refreshTokenDao;
    private ScheduledExecutorService executorService;

    @Autowired
    public RefreshTokenEventListener(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
    }

    @EventListener
    public void handleContextRefreshEvent(ContextRefreshedEvent event) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> refreshTokenDao.removeAllByExpiredTimeLessThan(new Date()),
                0, 11, TimeUnit.MINUTES);
    }

    @EventListener
    public void handleContextStoppedEvent(ContextStoppedEvent event) {
        executorService.shutdown();
    }
}
