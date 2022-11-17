package octoveau.sso.admin.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import octoveau.sso.admin.cache.SiteTokenCache;
import octoveau.sso.admin.constant.CommonConstants;

import java.util.concurrent.TimeUnit;

/**
 * SSOSiteTokenStorage
 *
 * @author yifanzheng
 */
public class SSOSiteTokenStorage {

    /**
     * Cache<#token, SiteTokenCache>
     */
    private final Cache<String, SiteTokenCache> tokenCache;

    public SSOSiteTokenStorage() {
        long tokenTTL = CommonConstants.DEFAULT_TOKEN_TTL;
        long refreshTokenTTL = CommonConstants.DEFAULT_REFRESH_TOKEN_TTL;
        tokenCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Math.max(tokenTTL, refreshTokenTTL), TimeUnit.SECONDS)
                .build();
    }

    public void cacheSiteToken(String token, SiteTokenCache siteTokenCache) {
        tokenCache.put(token, siteTokenCache);
    }

    public SiteTokenCache getCache(String token) {
        return tokenCache.getIfPresent(token);
    }

    public void remove(String token) {
        tokenCache.invalidate(token);
    }
}