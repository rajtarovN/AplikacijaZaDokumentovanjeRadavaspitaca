package com.diplomski.myapp.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.diplomski.myapp.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.diplomski.myapp.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.diplomski.myapp.domain.User.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Authority.class.getName());
            createCache(cm, com.diplomski.myapp.domain.User.class.getName() + ".authorities");
            createCache(cm, com.diplomski.myapp.domain.Korisnik.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Vaspitac.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Vaspitac.class.getName() + ".dnevniks");
            createCache(cm, com.diplomski.myapp.domain.Direktor.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Pedagog.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Objekat.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Objekat.class.getName() + ".potrebanMaterijals");
            createCache(cm, com.diplomski.myapp.domain.Objekat.class.getName() + ".vaspitacs");
            createCache(cm, com.diplomski.myapp.domain.Adresa.class.getName());
            createCache(cm, com.diplomski.myapp.domain.PotrebanMaterijal.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Dnevnik.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Dnevnik.class.getName() + ".pricas");
            createCache(cm, com.diplomski.myapp.domain.Dnevnik.class.getName() + ".neDolascis");
            createCache(cm, com.diplomski.myapp.domain.Dnevnik.class.getName() + ".vaspitacs");
            createCache(cm, com.diplomski.myapp.domain.Grupa.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Grupa.class.getName() + ".detes");
            createCache(cm, com.diplomski.myapp.domain.Roditelj.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Roditelj.class.getName() + ".detes");
            createCache(cm, com.diplomski.myapp.domain.Roditelj.class.getName() + ".formulars");
            createCache(cm, com.diplomski.myapp.domain.Prica.class.getName());
            createCache(cm, com.diplomski.myapp.domain.PlanPrice.class.getName());
            createCache(cm, com.diplomski.myapp.domain.KonacnaPrica.class.getName());
            createCache(cm, com.diplomski.myapp.domain.KomentarNaPricu.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Formular.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Formular.class.getName() + ".podaciORoditeljimas");
            createCache(cm, com.diplomski.myapp.domain.PodaciORoditeljima.class.getName());
            createCache(cm, com.diplomski.myapp.domain.ZapazanjeUVeziDeteta.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Dete.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Dete.class.getName() + ".zapazanjeUVeziDetetas");
            createCache(cm, com.diplomski.myapp.domain.NeDolasci.class.getName());
            createCache(cm, com.diplomski.myapp.domain.Prica.class.getName() + ".komentarNaPricus");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
