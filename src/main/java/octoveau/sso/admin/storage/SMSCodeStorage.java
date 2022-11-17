package octoveau.sso.admin.storage;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * SMSCodeStorage
 *
 * @author yifanzheng
 */
public class SMSCodeStorage {

    /**
     * Cache<#phone, code>
     */
    private final Cache<String, String> codeCache;

    public SMSCodeStorage() {
        codeCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    public void cacheCode(String phone, String code) {
        codeCache.put(phone, code);
    }

    public String getCache(String phone) {
        return codeCache.getIfPresent(phone);
    }
}
