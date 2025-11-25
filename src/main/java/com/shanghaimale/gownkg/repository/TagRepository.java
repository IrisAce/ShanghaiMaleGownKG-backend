package com.shanghaimale.gownkg.repository;

import com.shanghaimale.gownkg.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    // 根据标签名称查询
    Optional<Tag> findByName(String name);

    // 检查标签名称是否存在
    boolean existsByName(String name);
}