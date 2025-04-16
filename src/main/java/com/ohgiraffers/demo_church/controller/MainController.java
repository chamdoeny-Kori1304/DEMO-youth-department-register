package com.ohgiraffers.demo_church.controller;


// 참고 블로그 URL: https://cinnamon-lol.tistory.com/2
// https://minuk22.tistory.com/89

import com.ohgiraffers.demo_church.service.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/sheet/{range}")
    public ResponseEntity<?> getSheetData(@PathVariable String range) {
        String columName = "";
        try {
            final List<Map<String, String>> data= mainService.readFromSheet(range, columName);

            return ResponseEntity.ok(data);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to read data: " + e.getMessage());
        }
    }

    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {
        try {
            final List<Map<String, String>> data= mainService.readAllMemberFromMainSheet();

            return ResponseEntity.ok(data);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to get all member data: " + e.getMessage());
        }
    }
}