package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SubscriptionDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the SubscriptionDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubscriptionDetailsRepository
    extends ReactiveCrudRepository<SubscriptionDetails, Long>, SubscriptionDetailsRepositoryInternal {
    @Override
    <S extends SubscriptionDetails> Mono<S> save(S entity);

    @Override
    Flux<SubscriptionDetails> findAll();

    @Override
    Mono<SubscriptionDetails> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface SubscriptionDetailsRepositoryInternal {
    <S extends SubscriptionDetails> Mono<S> save(S entity);

    Flux<SubscriptionDetails> findAllBy(Pageable pageable);

    Flux<SubscriptionDetails> findAll();

    Mono<SubscriptionDetails> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<SubscriptionDetails> findAllBy(Pageable pageable, Criteria criteria);
}
