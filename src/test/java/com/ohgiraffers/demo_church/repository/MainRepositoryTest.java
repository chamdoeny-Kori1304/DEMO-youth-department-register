package com.ohgiraffers.demo_church.repository;

import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class MainRepositoryTest {
//    private final MainRepository mainRepository = new MainRepository(new GoogleSheetConfig());

    @Autowired
    private MainRepository mainRepository;

    @Value("${google.spreadsheet.main}!${google.spreadsheet.main.range}")
    private String MAIN_SHEET_RANGE;

    @Value("${google.spreadsheet.member.count}")
    private int MEMBER_COUNT;

    private String MAIN_DEFAULT_COLUMN_NAME = "이름";

    @Test
    void 모든_멤버_찾는데_걸리는_시간() {
        long start = System.nanoTime();

        List<Map<String, String>> result = mainRepository.findData(MAIN_SHEET_RANGE, MAIN_DEFAULT_COLUMN_NAME);

        long end = System.nanoTime();
        double elapsedMillis = (end - start) / 1_000_000.0;

        log.info("findAll() 실행 시간: {} ms", elapsedMillis);
        log.info("가져온 행 수: {}", result.size());
        log.info("MAIN_SHEET_RANGE: {} ", MAIN_SHEET_RANGE);
    }

    @Test
    void checkAllMemberCount() {
        List<Map<String, String>> result = mainRepository.findData(MAIN_SHEET_RANGE, MAIN_DEFAULT_COLUMN_NAME);

        assertEquals(MEMBER_COUNT, result.size()); // 헤더 제외한 row 2개

    }

    @Test
    void whenValidRange_thenReturnsMappedList() {
        String range = "Sheet1!A1:C3"; // 실제 데이터가 있는 곳
        List<Map<String, String>> result = mainRepository.findData(range, "");

        assertEquals(2, result.size()); // 헤더 제외한 row 2개
        assertTrue(result.get(0).containsKey("1111")); // 헤더 예시
    }

    @Test
    void whenEmptyDataRange_thenReturnsEmptyList() {
        String range = "Sheet1!Z1:Z3"; // 비어 있는 영역
        List<Map<String, String>> result = mainRepository.findData(range,"");

        assertTrue(result.isEmpty());
    }

    @Test
    void whenHeaderOnly_thenReturnsEmptyList() {
        String range = "Sheet1!A1:C1"; // 헤더만 있음
        List<Map<String, String>> result = mainRepository.findData(range, "");

        assertTrue(result.isEmpty());
    }

    @Test
    void whenPartialRow_thenReturnsEmptyStringForMissingColumns() {
        String range = "Sheet1!A1:C4"; // 마지막 row가 일부만 있음
        List<Map<String, String>> result = mainRepository.findData(range, "");

        for (Map<String, String> row : result) {
            assertEquals(3, row.size()); // 모든 row는 헤더 수만큼 키를 가져야 함
        }
    }

    @Test
    void whenInvalidRange_thenThrowsException() {
        String range = "InvalidSheet!A1:B2";
        assertThrows(RuntimeException.class, () -> mainRepository.findData(range,""));
    }
}