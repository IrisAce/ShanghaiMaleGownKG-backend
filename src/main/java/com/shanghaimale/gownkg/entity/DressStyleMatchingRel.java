package com.shanghaimale.gownkg.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "dress_style_matching")
@Data
public class DressStyleMatchingRel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer styleId;
    private Integer matchingId;
}