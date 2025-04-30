package com.ohgiraffers.demo_church.domain;

import com.ohgiraffers.demo_church.domain.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    private String id;
    private String name;
    private Gender gender;
    private String phoneNumber;
    private String address;
    private LocalDate birthDate;
    private String humanRelations;
    private String teamName;


    public List<Object> toList(){
        List<Object> list = new ArrayList<>();
        list.add(""); // A열을 비워야 하기때무에 더비 데이터 작성
        list.add(name);
        list.add(birthDate.toString());
        list.add(phoneNumber);
        list.add(humanRelations);
        list.add(address);
        list.add(gender.toString());
        list.add(teamName);
        return list;
    }


    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", humanRelations='" + humanRelations + '\'' +
                ", teamName='" + teamName + '\'' +
                '}';
    }

}


