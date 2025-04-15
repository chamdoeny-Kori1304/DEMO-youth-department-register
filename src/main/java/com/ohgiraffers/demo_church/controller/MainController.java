package com.ohgiraffers.demo_church.controller;


// 참고 블로그 URL: https://cinnamon-lol.tistory.com/2
// https://minuk22.tistory.com/89

import com.ohgiraffers.demo_church.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @Value("${google.spreadsheet.id}")  // <-- 이렇게 수정해야 동작함
    private String SPREAD_SHEET_ID;

    private static final String RANGE = "sheet1!A1:E1";

    @GetMapping("/read")
    public ResponseEntity<?> readFromSheet() {

        try {
            final List<Map<String, String>> data= mainService.readFromSheet(SPREAD_SHEET_ID, RANGE);

            return ResponseEntity.ok(data);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to read data: " + e.getMessage());
        }
    }
}