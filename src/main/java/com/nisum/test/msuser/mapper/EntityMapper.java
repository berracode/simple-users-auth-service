package com.nisum.test.msuser.mapper;

import java.util.List;
import java.util.Set;

/**
 * EntityMapper.
 * @param <D> DTO class
 * @param <E> Entity class
 * @author Juan Hernandez.
 * @version 1.0.0, 03-10-2024
 */
public interface EntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntity(List<D> dtoList);

    List<D> toDto(List<E> entityList);

    Set<E> toEntity(Set<D> dtoList);

    Set<D> toDto(Set<E> entityList);
}