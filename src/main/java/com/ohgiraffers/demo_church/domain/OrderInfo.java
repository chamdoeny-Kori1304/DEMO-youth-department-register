package com.ohgiraffers.demo_church.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderInfo {
    private Long id;
    private Long itemId;
    private LocalDateTime orderDate;
}
