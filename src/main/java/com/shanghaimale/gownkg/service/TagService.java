package com.shanghaimale.gownkg.service;

import com.shanghaimale.gownkg.entity.Tag;
import com.shanghaimale.gownkg.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    // 获取所有标签
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    // 根据ID获取标签
    public Optional<Tag> getTagById(Long id) {
        return tagRepository.findById(id);
    }

    // 创建标签
    @Transactional
    public Tag createTag(Tag tag) {
        if (tagRepository.existsByName(tag.getName())) {
            throw new IllegalArgumentException("标签名称已存在");
        }
        return tagRepository.save(tag);
    }

    // 更新标签
    @Transactional
    public Tag updateTag(Long id, Tag tag) {
        return tagRepository.findById(id)
                .map(existingTag -> {
                    if (!existingTag.getName().equals(tag.getName()) &&
                            tagRepository.existsByName(tag.getName())) {
                        throw new IllegalArgumentException("标签名称已存在");
                    }
                    existingTag.setName(tag.getName());
                    existingTag.setDescription(tag.getDescription());
                    return tagRepository.save(existingTag);
                })
                .orElseThrow(() -> new IllegalArgumentException("标签不存在"));
    }

    // 删除标签
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("标签不存在");
        }
        tagRepository.deleteById(id);
    }
}