package com.ohgiraffers.demo_church.domain;

import com.ohgiraffers.demo_church.domain.enums.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Member {
    private String id;
    private String name;
    private String phoneNumber;
    private String address;
    private Gender gender;
    private LocalDate birthDate;
    private String humanRelations;
    private String teamName;


    public List<Object> toList(){
        List<Object> list = new ArrayList<>();
        list.add(""); // A열을 비워야 하기때무에 더비 데이터 작성
        list.add(id);
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


    public Member() {}

    public Member(String id, String name, String phoneNumber,  String address, Gender gender, LocalDate birthDate, String  humanRelations, String teamName) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
        this.humanRelations =  humanRelations;
        this.teamName = teamName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setHumanRelations(String humanRelations) {
        this.humanRelations = humanRelations;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getHumanRelations() {
        return humanRelations;
    }

    public String getTeamName() {
        return teamName;
    }
}


