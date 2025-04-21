package com.ohgiraffers.demo_church.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
class AttendanceRepositoryTest {

    //TODO 싫행하기 전에 특정 날짜에 값들을  "null"로 바꾸자

    // TODO 테스트용 시트로 바꿔서 사용, 실제 사용 시트는 건들지 않게 수정

    @Autowired
    private AttendanceRepository attendanceRepository;
    static private String[] DEFAULT_TARGET_NAMES = {"성종민9", "김철수1", "신짱구5", "김철수9", "노진구5", "홍길동1"};
    static private String DEFAULT_TARGET_DATE = "1/5";

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
    void matchAttendance () {
        List<String> expect = new ArrayList<>((Arrays.asList(DEFAULT_TARGET_NAMES)));
        long start = System.nanoTime();

        List<String> names =attendanceRepository.getNamesByDate(DEFAULT_TARGET_DATE);
        long end = System.nanoTime();
        double elapsedMillis = (end - start) / 1_000_000.0;

        Collections.sort(names);
        Collections.sort(expect);

        assertEquals(expect, names);

        log.info("writeAttendance() 실행 시간: {} ms", elapsedMillis);
    }
}