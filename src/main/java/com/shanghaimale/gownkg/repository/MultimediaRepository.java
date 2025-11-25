package com.shanghaimale.gownkg.repository;

import com.shanghaimale.gownkg.entity.Multimedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultimediaRepository extends JpaRepository<Multimedia, Integer> {
    List<Multimedia> findByIdIn(List<Integer> ids);
}