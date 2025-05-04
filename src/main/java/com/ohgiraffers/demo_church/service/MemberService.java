package com.ohgiraffers.demo_church.service;

import com.ohgiraffers.demo_church.common.SheetResponse;
import com.ohgiraffers.demo_church.domain.Member;
import com.ohgiraffers.demo_church.repository.AttendanceRepository;
import com.ohgiraffers.demo_church.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;
    private final AttendanceRepository attendanceRepository;


    private final String DEFAULT_COLUMN_NAME = "이름";


    // TODO 테스트 코드 만들기
    public String saveMember(Member member) throws IOException, GeneralSecurityException {
        List<Object> list = new ArrayList<>(); // 빈 ArrayList 생성

        SheetResponse sheetResponse = repository.save(member);
        String updatedRange = sheetResponse.getUpdatedRange();

        list.add(""); // sheet 1번째 열을 비우기 위해서
        list.add(member.getName()); // 초기 값 추가
        attendanceRepository.addRow(list); // TODO AttendanceService를 만다는 것이 통일성에 좋은가?? => 객체에 쓰임 문서로 작성하기
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