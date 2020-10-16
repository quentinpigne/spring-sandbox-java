package fr.quentinpigne.springsandboxjava.configuration;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEventLogger implements CacheEventListener<Object, Object> {

    private final Logger logger = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent cacheEvent) {
        logger.info("Cache event = {}, Key = {},  Old value = {}, New value = {}", cacheEvent.getType(), cacheEvent.getKey(),
            cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
