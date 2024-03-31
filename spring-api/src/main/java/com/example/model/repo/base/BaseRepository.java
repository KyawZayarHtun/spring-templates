package com.example.model.repo.base;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepositoryImplementation<T, ID> {

    <R> Optional<R> findOne(Function<CriteriaBuilder, CriteriaQuery<R>> searchFunction);
    <R> List<R> findAll(Function<CriteriaBuilder, CriteriaQuery<R>> searchFunction);
    <R> Page<R> findAll(Function<CriteriaBuilder, CriteriaQuery<R>> searchFunction,
                        Function<CriteriaBuilder, CriteriaQuery<Long>> countFunction,
                        int pageNo, int size);

}
