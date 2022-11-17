package octoveau.sso.admin.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import octoveau.sso.admin.cache.SiteCache;
import octoveau.sso.admin.constant.CommonConstants;

import java.util.concurrent.TimeUnit;

public class SSOSiteTicketStorage {

    /**
     * Cache<#ticket, SiteCache>
     */
    private final Cache<String, SiteCache> ticketCache;

    public SSOSiteTicketStorage() {
        long ticketTTL = CommonConstants.DEFAULT_TICKET_TTL;
        ticketCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(ticketTTL, TimeUnit.SECONDS)
                .build();
    }

    public void cacheSite(String ticket, SiteCache siteCache) {
        ticketCache.put(ticket, siteCache);
    }

    public SiteCache getCache(String ticket) {
        return ticketCache.getIfPresent(ticket);
    }
}
