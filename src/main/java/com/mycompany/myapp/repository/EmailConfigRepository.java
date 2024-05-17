package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.EmailConfig;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the EmailConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailConfigRepository extends ReactiveCrudRepository<EmailConfig, Long>, EmailConfigRepositoryInternal {
    @Override
    <S extends EmailConfig> Mono<S> save(S entity);

    @Override
    Flux<EmailConfig> findAll();

    @Override
    Mono<EmailConfig> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface EmailConfigRepositoryInternal {
    <S extends EmailConfig> Mono<S> save(S entity);

    Flux<EmailConfig> findAllBy(Pageable pageable);

    Flux<EmailConfig> findAll();

    Mono<EmailConfig> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<EmailConfig> findAllBy(Pageable pageable, Criteria criteria);
}
