package com.shanghaimale.gownkg.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

enum DressType {
    官服, 民服, 中式, 西式, 中西融合式, 五服制
}


@Entity
@Table(name = "dress_base_info")
@Data
public class DressBaseInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dressId;
    private String dressName;
    private String dressIntroduction;
    @Enumerated(EnumType.STRING)
    private DressType dressType;
    @Enumerated(EnumType.STRING)
    private BelongingPeriod belongingPeriod;
    private String image;
    private String threeDModel;
    private String video;
    private String dressImg;

    @OneToMany(mappedBy = "dressBaseInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DressStyleInfo> dressStyleInfos;

    @OneToMany(mappedBy = "dressBaseInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DressMatchingInfo> dressMatchingInfos;
}

