package com.ohgiraffers.demo_church.domain.enums;

import org.springframework.beans.factory.annotation.Value;

//public enum Gender {
//    @Value("남자")
//    MALE,
//    @Value("여자")
//    FEMALE
//}



public enum Gender {
    MALE("남자"),
    FEMALE("여자");

    private final String koreanValue;

    Gender(String koreanValue) {
        this.koreanValue = koreanValue;
    }

    @Override
    public String toString() {
        return koreanValue;
    }
}