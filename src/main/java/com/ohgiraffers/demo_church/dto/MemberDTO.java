package com.ohgiraffers.demo_church.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ohgiraffers.demo_church.domain.Member;
import com.ohgiraffers.demo_church.domain.enums.Gender;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private String name;
    private Gender gender;
    private String phoneNumber;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String humanRelations;
    private String teamName;

    public MemberDTO(String name,Gender gender ) {
        this.name = name;
        this.gender = gender;
    }


    public Member toMember() {
        return Member.builder()
                .id("") // id 보존
                .name(this.name)
                .gender(this.gender)
                .phoneNumber(this.phoneNumber != null ? this.phoneNumber :"")
                .address(this.address != null ? this.address : "")
                .birthDate(this.birthDate != null ? this.birthDate : LocalDate.now())
                .humanRelations(this.humanRelations != null ? this.humanRelations : "")
                .teamName(this.teamName != null ? this.teamName : "")
                .build();
    }

}
