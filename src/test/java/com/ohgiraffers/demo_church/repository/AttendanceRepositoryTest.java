package com.ohgiraffers.demo_church.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@TestPropertySource(properties = {
        "google.spreadsheet.Attendance.range=A1:AA90",
        "google.spreadsheet.Attendance=sheet2"
})
class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;
    static private String[] DEFAULT_TARGET_NAMES = {"성종민9", "김철수1", "신짱구5", "김철수9", "노진구5", "홍길동1"};
    static private String DEFAULT_TARGET_DATE = "1/5";

    @Test
    void testSheetRangeAndNameFromTestConfig() {
        System.out.println("Sheet Range in Test: " + attendanceRepository.getSheetRange());

        assertEquals("sheet2!A1:AA90", attendanceRepository.getSheetRange());
    }

    @Test
    void writeAttendance() {
        long start = System.nanoTime();

        attendanceRepository.updateAttendance(DEFAULT_TARGET_NAMES, DEFAULT_TARGET_DATE);

        long end = System.nanoTime();
        double elapsedMillis = (end - start) / 1_000_000.0;

        log.info("writeAttendance() 실행 시간: {} ms", elapsedMillis);
    }

    /**
     * 지정된 날짜에 출석한 인원의 이름을 검사
     * !주의! writeAttendance()를 실행하고 나서 사용해야 에러 안 남
     */
    @Test
    void matchAttendance() {
        List<String> expect = new ArrayList<>((Arrays.asList(DEFAULT_TARGET_NAMES)));
        long start = System.nanoTime();

        List<String> names = attendanceRepository.getNamesByDate(DEFAULT_TARGET_DATE);
        long end = System.nanoTime();
        double elapsedMillis = (end - start) / 1_000_000.0;

        Collections.sort(names);
        Collections.sort(expect);

        assertEquals(expect, names);

        log.info("writeAttendance() 실행 시간: {} ms", elapsedMillis);
    }

    @Test
    void AddRowTest() throws IOException, GeneralSecurityException {

        List<Object> newData = new ArrayList<>(); // 빈 ArrayList 생성
        newData.add("김세나"); // 초기 값 추가

        attendanceRepository.addRow(newData);

    }
}