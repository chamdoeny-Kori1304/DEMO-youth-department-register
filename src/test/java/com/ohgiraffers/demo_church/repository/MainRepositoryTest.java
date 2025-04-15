package com.ohgiraffers.demo_church.repository;

import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import org.junit.jupiter.api.Test;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;


@SpringBootTest
@Slf4j
class MainRepositoryTest {
    private final MainRepository mainRepository = new MainRepository(new GoogleSheetConfig());

    @Value("${google.spreadsheet.main}!${google.spreadsheet.main.range}")
    private String MAIN_SHEET_RANGE;

    @Test
    void findAll_실행시간_측정() {
        long start = System.nanoTime();

        List<Map<String, String>> result = mainRepository.findData(MAIN_SHEET_RANGE);

        long end = System.nanoTime();
        double elapsedMillis = (end - start) / 1_000_000.0;

        log.info("findAll() 실행 시간: {} ms", elapsedMillis);
        log.info("가져온 행 수: {}", result.size());
        log.info("MAIN_SHEET_RANGE: {} ", MAIN_SHEET_RANGE);
    }
}