package com.ohgiraffers.demo_church.repository;

import com.ohgiraffers.demo_church.common.SheetResponse;
import com.ohgiraffers.demo_church.domain.Member;
import com.ohgiraffers.demo_church.domain.enums.Gender;
import com.ohgiraffers.demo_church.util.ReflectionUtil;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
@TestPropertySource(properties = {
        "google.spreadsheet.member.range=A1:M90",
        "google.spreadsheet.member=sheet1"
})
class MemberRepositoryTest {
    @Autowired
    private MemberRepository repository;

    @Value("${google.spreadsheet.member.count}")
    private int MEMBER_COUNT; // TODO DB에서 새로 추가되면 자동으로 count가 변해야 한다.

    private String MAIN_DEFAULT_COLUMN_NAME = "이름";

    static Member member1 = new Member("user01", "김철수", Gender.MALE,"010-1234-5678", "서울시 강남구", LocalDate.of(1990, 5, 15), "친구", "조효린");
    static Member member2 = new Member("user02", "이영희", Gender.FEMALE, "010-1111-5678","경기도 성남시",  LocalDate.of(1995, 10, 20), "동료", "성종민");
    static Member member3 = new Member("user03", "박지성", Gender.MALE, "010-2222-5678","인천광역시",  LocalDate.of(1988, 7, 1), "가족", "박용진");
    static Member member4 = new Member("user04", "최유나", Gender.FEMALE, "010-3333-5678","대전광역시", LocalDate.of(2000, 3, 10), "선배", "정현빈");

    @Test
    void 모든_멤버_찾는데_걸리는_시간() {
        long start = System.nanoTime();

        List<Map<String, String>> result = repository.findData( MAIN_DEFAULT_COLUMN_NAME);

        long end = System.nanoTime();
        double elapsedMillis = (end - start) / 1_000_000.0;

        log.info("findAll() 실행 시간: {} ms", elapsedMillis);
        log.info("가져온 행 수: {}", result.size());
        log.info("MAIN_SHEET_RANGE: {} ", repository.getSheetRange());
    }

    @Test
    void checkAllMemberCount() {
        List<Map<String, String>> result = repository.findData( MAIN_DEFAULT_COLUMN_NAME);

        assertEquals(6, result.size()); // 헤더 제외한 row 2개

    }

    @Test
    void whenValidRange_thenReturnsMappedList() {
        String range = "Sheet1!A1:J3"; // 실제 데이터가 있는 곳
        List<Map<String, String>> result = repository.findData( "");

        assertEquals(6, result.size()); // 헤더 제외한 row 2개
        assertTrue(result.get(0).containsKey("현재 셀")); // 헤더 예시
    }


    @Test
    void whenPartialRow_thenReturnsEmptyStringForMissingColumns() {
        List<Map<String, String>> result = repository.findData( "");

        int fieldCount = ReflectionUtil.getFieldCount(Member.class);

        for (Map<String, String> row : result) {
            // 출석률과 메모는 Member에 포함될 필요 없음, 그렇기에 +2를 추가
            assertEquals(fieldCount +2 , row.size()); // 모든 row는 헤더 수만큼 키를 가져야 함
        }
    }

    @Test
    void addNewMember_thenReturnsTrue() {

       SheetResponse res = repository.save(member2);
       assertTrue(res.isSuccess());
    }
}