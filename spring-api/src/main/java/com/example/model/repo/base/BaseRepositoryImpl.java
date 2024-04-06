package com.example.model.repo.base;


import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }


    @Override
    public <R> Optional<R> findOne(Function<CriteriaBuilder, CriteriaQuery<R>> searchFunction) {
        var cq = searchFunction.apply(entityManager.getCriteriaBuilder());
        return entityManager.createQuery(cq).getResultStream().findFirst();
    }

    @Override
    public <R> List<R> findAll(Function<CriteriaBuilder, CriteriaQuery<R>> searchFunction) {
        var cq = searchFunction.apply(entityManager.getCriteriaBuilder());
        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public <R> Page<R> findAll(Function<CriteriaBuilder, CriteriaQuery<R>> searchFunction,
                               Function<CriteriaBuilder, CriteriaQuery<Long>> countFunction,
                               int pageNo, Integer size) {
        var totalCount = findOne(countFunction).orElse(0L);

        var cq = searchFunction.apply(entityManager.getCriteriaBuilder());
        var query = entityManager.createQuery(cq);

        if (size == null || size <= 0)
            size = Integer.MAX_VALUE;

        query.setFirstResult((pageNo - 1) * size);
        query.setMaxResults(size);

        return new PageImpl<>(query.getResultList(), PageRequest.of(pageNo - 1, size), totalCount);
    }

}
