package com.shanghaimale.gownkg.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "dress_matching_info")
@Data
public class DressMatchingInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchingId;
    private String matchingName;
    private String matchingIntroduction;
    private String matchingComposition;
    @Enumerated(EnumType.STRING)
    private DressType styleType;
    @Enumerated(EnumType.STRING)
    private BelongingPeriod belongingPeriod;
    private String image;
    private String threeDModel;
    private String video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dress_id")
    @JsonBackReference
    private DressBaseInfo dressBaseInfo;
}