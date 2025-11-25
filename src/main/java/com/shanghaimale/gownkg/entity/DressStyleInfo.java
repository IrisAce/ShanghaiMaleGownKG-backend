package com.shanghaimale.gownkg.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

enum StyleCategory {
    主体款式, 服饰品, 成套
}

@Entity
@Table(name = "dress_style_info")
@Data
public class DressStyleInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer styleId;
    private String styleName;
    private String styleIntroduction;
    @Enumerated(EnumType.STRING)
    private StyleCategory styleCategory;
    @Enumerated(EnumType.STRING)
    private DressType styleType;
    @Enumerated(EnumType.STRING)
    private BelongingPeriod belongingPeriod;
    private String matchingId;
    private String styleTag;
    private String image;
    private String threeDModel;
    private String video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dress_id")
    @JsonBackReference
    private DressBaseInfo dressBaseInfo;
}