package com.ohgiraffers.demo_church.service;

import com.ohgiraffers.demo_church.common.SheetResponse;
import com.ohgiraffers.demo_church.domain.Member;
import com.ohgiraffers.demo_church.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;


    private final String DEFAULT_COLUMN_NAME = "이름";


    public String saveMember(Member member) {
        SheetResponse sheetResponse = repository.save(member);
        String updatedRange = sheetResponse.getUpdatedRange();
        return updatedRange;
    }



    /**
     * 모든 회원 데이터를 Member Sheet에서 읽어옵니다.
     *
     * @return 회원 데이터 전체 리스트
     */
    public List<Map<String, String>> readAllMembersFromMainSheet() {
        return repository.findData(DEFAULT_COLUMN_NAME);

    }

    /**
     * 모든 회원 데이터를 Member Sheet에서 읽어옵니다.
     *
     * @return 회원을 "현재 셀"로 분류한 데이터
     */
    public Map<String, List<String>> getTeamToMembersMap() {

        return repository.getTeamToMembersMap(DEFAULT_COLUMN_NAME, "현재 셀");

    }
}