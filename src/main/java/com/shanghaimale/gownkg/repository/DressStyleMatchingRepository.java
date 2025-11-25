package com.shanghaimale.gownkg.repository;

import com.shanghaimale.gownkg.entity.DressStyleInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DressStyleMatchingRepository extends JpaRepository<DressStyleInfo, Integer> {

}