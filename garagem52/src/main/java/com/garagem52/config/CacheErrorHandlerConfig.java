package com.garagem52.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

/**
 * Cache é otimização, não requisito: se o Redis estiver fora do ar (ou uma entrada
 * antiga não puder ser desserializada depois de um deploy), o erro é apenas logado
 * e a requisição segue normalmente pelo MongoDB.
 */
@Configuration
public class CacheErrorHandlerConfig implements CachingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CacheErrorHandlerConfig.class);

    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.warn("Falha ao ler o cache '{}' (chave {}): {} — seguindo pelo banco",
                        cache.getName(), key, e.toString());
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.warn("Falha ao gravar no cache '{}' (chave {}): {}", cache.getName(), key, e.toString());
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
                log.warn("Falha ao invalidar o cache '{}' (chave {}): {}", cache.getName(), key, e.toString());
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.warn("Falha ao limpar o cache '{}': {}", cache.getName(), e.toString());
            }
        };
    }
}
