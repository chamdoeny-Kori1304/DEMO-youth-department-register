package com.ohgiraffers.demo_church.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAttendanceDTO {
    private LocalDate date;
    private String[] names;
}
