package net.nickreuter.branch_coding_exercise.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Configuration
@EnableCaching
@Component
public class CacheConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @CacheEvict(allEntries = true, cacheNames = { "gitHubProfile", "gitHubRepositories" })
    @Scheduled(cron = "0 0 * * * *")
    public void cacheEvict() {
        logger.debug("Cache Evicted");
    }
}
