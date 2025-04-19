package com.ohgiraffers.demo_church.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import com.ohgiraffers.demo_church.util.GoogleSheetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final GoogleSheetConfig googleSheetConfig;
    private final GoogleSheetUtils googleSheetUtils;

    @Value("${google.spreadsheet.id}")
    private String SPREAD_SHEET_ID;
    @Value("${google.spreadsheet.main}!${google.spreadsheet.main.range}")
    private final String sheetName = "sheet2";

    public void updateAttendance(String targetName, String targetDate)  {
        Sheets sheets;
        List<List<Object>> values;

        try {
            sheets = googleSheetConfig.provideSheetsClient();
            values = googleSheetUtils.filterData(sheets,"sheet2!A2:AA10", SPREAD_SHEET_ID);

            if (values == null || values.isEmpty()) return;

            List<Object> header = values.get(0);
            int colIndex = header.indexOf(targetDate);
            if (colIndex == -1) throw new IllegalArgumentException("날짜가 존재하지 않습니다: " + targetDate);

            // Step 1: 이름(행) 인덱스 찾기
            int rowIndex = -1;
            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);
                if (!row.isEmpty() && row.get(0).toString().equals(targetName)) {
                    rowIndex = i;
                    break;
                }
            }
            if (rowIndex == -1) throw new IllegalArgumentException("이름이 존재하지 않습니다: " + targetName);

            // Step 2: "A1" 표기법으로 셀 위치 계산
            String cell = getA1Notation(colIndex, rowIndex);
            String cellRange = sheetName + "!" + cell;

            // Step 3: 해당 셀에 O 입력
            ValueRange valueRange = new ValueRange()
                    .setRange(cellRange)
                    .setValues(List.of(List.of("O")));

            sheets.spreadsheets().values()
                    .update(SPREAD_SHEET_ID, cellRange, valueRange)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

       ;
    }

    private String getA1Notation(int colIndex, int rowIndex) {
        return columnToLetter(colIndex) + (rowIndex + 1 +1); // rowIndex는 0-based
    }

    private String columnToLetter(int column) {
        StringBuilder sb = new StringBuilder();
        while (column >= 0) {
            sb.insert(0, (char) ('A' + column % 26));
            column = column / 26 - 1;
        }
        return sb.toString();
    }
}



