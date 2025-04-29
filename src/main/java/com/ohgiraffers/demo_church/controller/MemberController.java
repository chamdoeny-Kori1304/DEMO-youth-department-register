package com.ohgiraffers.demo_church.controller;


// 참고 블로그 URL: https://cinnamon-lol.tistory.com/2
// https://minuk22.tistory.com/89

import com.ohgiraffers.demo_church.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/google")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService mainService;

    @GetMapping("/sheet/{range}")
    public ResponseEntity<?> getSheetData(@PathVariable String range, @RequestParam(required = false) String columnName) {

        try {
            final List<Map<String, String>> data = mainService.readFromSheet(range, columnName);

            return ResponseEntity.ok(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to read data: " + e.getMessage());
        }
    }

    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers() {
        try {
            final
            List <Map<String, String>> data = mainService.readAllMembersFromMainSheet();

            return ResponseEntity.ok(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to get all member data: " + e.getMessage());
        }
    }


    @GetMapping("/team-members")
    public ResponseEntity<?> getCurrentTeamMembers() {
        String yearQuarter = "1분기 셀"; // TODO DB column 이름을 변경하면 동일하게 변경
        try {
            final
            Map<String, List<String>>  data = mainService.getTeamToMembersMap(yearQuarter);

            return ResponseEntity.ok(data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to get all member data: " + e.getMessage());
        }
    }
}