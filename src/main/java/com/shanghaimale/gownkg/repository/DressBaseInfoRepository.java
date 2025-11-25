package com.shanghaimale.gownkg.repository;


import com.shanghaimale.gownkg.entity.BelongingPeriod;
import com.shanghaimale.gownkg.entity.DressBaseInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DressBaseInfoRepository extends JpaRepository<DressBaseInfo, Integer> {
    List<DressBaseInfo> findByBelongingPeriod(BelongingPeriod belongingPeriod);
}