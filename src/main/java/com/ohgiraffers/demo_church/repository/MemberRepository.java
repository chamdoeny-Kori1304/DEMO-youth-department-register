package com.ohgiraffers.demo_church.repository;

import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.common.SheetResponse;
import com.ohgiraffers.demo_church.common.service.GoogleSheetsService;
import com.ohgiraffers.demo_church.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.util.*;


@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final GoogleSheetsService sheetsService;

    @Getter
    @Value("${google.spreadsheet.member}")
    private String sheetName;

    @Getter
    @Value("${google.spreadsheet.member}!${google.spreadsheet.member.range}")
    private String sheetRange;

//    private String range = "구성원!A1J2";

    // TODO 이미 존재하는지 확인 후 동작 로직 추가
    public SheetResponse save( Member member) {
        try {
            List<List<Object>> values = List.of(member.toList());
            ValueRange body = new ValueRange().setValues(values);
            String appendRange = sheetName + "!" + "A1:M1";
            AppendValuesResponse res = sheetsService.appendSheetData(appendRange, body);

            return new SheetResponse(true, res.getUpdates().getUpdatedRange());

        } catch (Exception e) {
            log.error("Failed to write data to the spreadsheet", e);
            throw new RuntimeException("Failed to write data to the spreadsheet: " + e.getMessage(), e);
        }
    }

    public List<Map<String, String>> findData(String defaultColumnName) {
        List<Map<String, String>> findResponse = new ArrayList<>();
        try {
            List<Map<String, String>> _res = sheetsService.findData(sheetRange, defaultColumnName);
            if (_res != null && !_res.isEmpty()) {
                findResponse.addAll(_res);
            }
        } catch (Exception e) {
            log.error("Failed to read data from the spreadsheet", e);
            throw new RuntimeException("Failed to read data from the spreadsheet: " + e.getMessage(), e);
        }

        return findResponse;
    }

    public Map<String, List<String>> getTeamToMembersMap( String defaultColumnName, String yearQuarter) {
        Map<String, List<String>> teamToMembersMap = new HashMap<>();
        sheetsService.getDataFromSheet(sheetRange, defaultColumnName, (rowMap) -> {
            String key = rowMap.get(yearQuarter);
            if (!teamToMembersMap.containsKey(key)) {
                teamToMembersMap.put(key, new ArrayList<>());
            }
            teamToMembersMap.get(key).add(rowMap.get("이름"));
            return teamToMembersMap;
        });
        return teamToMembersMap;
    }

}
