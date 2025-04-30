package com.ohgiraffers.demo_church.service;

import com.ohgiraffers.demo_church.domain.Member;
import com.ohgiraffers.demo_church.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository repository;

    @Value("${google.spreadsheet.main}!${google.spreadsheet.main.range}")
    private String ALL_MEMBER_RANGE;

    private final String DEFAULT_COLUMN_NAME = "이름";

    public void saveMember(String range, Member member) {
        repository.save(member);

    }

    /**
     * 지정된 범위의 Sheet 데이터를 읽어옵니다.
     *
     * @param range 읽어올  Sheet의 범위 (예: "Sheet1!A1:D100")
     * @return 해당 범위의 데이터를 Map 리스트 형식으로 반환
     */
    public List<Map<String, String>> readFromSheet(String range, String defaultColumName) {

        return repository.findData( defaultColumName);

    }

    /**
     * 모든 회원 데이터를 Main Sheet에서 읽어옵니다.
     *
     * @return 회원 데이터 전체 리스트
     */
    public List<Map<String, String>> readAllMembersFromMainSheet() {
        return repository.findData( DEFAULT_COLUMN_NAME);

    }


    public Map<String, List<String>> getTeamToMembersMap(String yearQuarter) {
        return repository.getTeamToMembersMap( DEFAULT_COLUMN_NAME, yearQuarter);

    }
}