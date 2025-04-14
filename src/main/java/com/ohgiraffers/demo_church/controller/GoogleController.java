package com.ohgiraffers.demo_church.controller;


// 참고 블로그 URL: https://cinnamon-lol.tistory.com/2
import com.google.api.client.util.Value;
import com.ohgiraffers.demo_church.domain.OrderInfo;
import com.ohgiraffers.demo_church.service.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.time.LocalDateTime.now;

@Slf4j
@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class GoogleController {
    private final GoogleService googleService;

    @Value("${google.spreadsheet.id}")
    private String SPREAD_SHEET_ID;
    private static final String RANGE = "sheet1!A1:C1";

    @PostMapping("/write")
    public ResponseEntity<?> writeToSheet() {
        try {
            OrderInfo orderInfo = OrderInfo.builder()
                    .id(1L)
                    .itemId(0L)
                    .orderDate(now())
                    .build();
            googleService.writeToSheet(SPREAD_SHEET_ID, RANGE, orderInfo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to write data: " + e.getMessage());
        }
    }
}