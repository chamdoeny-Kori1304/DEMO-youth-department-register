package com.ohgiraffers.demo_church.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import com.ohgiraffers.demo_church.util.GoogleSheetUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
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

    // TODO 구글시트에 이름을 사전순으로 정렬하고 targetNames도 사전순으로 정렬해서 하면 O(n)으로 끝낼 수 있다.
    public void updateAttendance(String[] targetNames, String targetDate) {
        Sheets sheets;
        List<List<Object>> values;
        Arrays.sort(targetNames);


        try {
            sheets = googleSheetConfig.provideSheetsClient();
            values = googleSheetUtils.filterData(sheets, "sheet2!A1:AA90", SPREAD_SHEET_ID);

            if (values == null || values.isEmpty()) return;

            List<Object> header = values.get(0);
            int colIndex = header.indexOf(targetDate);
            if (colIndex == -1) throw new IllegalArgumentException("날짜가 존재하지 않습니다: " + targetDate);

            List<ValueRange> updateData = new ArrayList<>();
            for (int rowIdx = 1; rowIdx < values.size(); rowIdx++) {
                List<Object> row = values.get(rowIdx);
                if (row.isEmpty()) continue;
                String currentName = row.get(0).toString();


                if (isContainName(targetNames, currentName)) {
                    String cell = getA1Notation(colIndex, rowIdx);
                    String cellRange = sheetName + "!" + cell;

                    // 해당 셀에 O 입력할 것을 저장
                    ValueRange valueRange = new ValueRange()
                            .setRange(cellRange)
                            .setValues(List.of(List.of("O")));
                    updateData.add(valueRange);
                }
            }

            // BatchUpdateValuesRequest를 사용하면 복수에 데이터 update 가능
            BatchUpdateValuesRequest body = new BatchUpdateValuesRequest()
                    .setValueInputOption("RAW")
                    .setData(updateData);

            sheets.spreadsheets().values()
                    .batchUpdate(SPREAD_SHEET_ID, body)
                    .execute();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ;
    }





    private String getA1Notation(int colIndex, int rowIndex) {
        return columnToLetter(colIndex) + (rowIndex + 1); // rowIndex는 0-based
    }

    private String columnToLetter(int column) {
        StringBuilder sb = new StringBuilder();
        while (column >= 0) {
            sb.insert(0, (char) ('A' + column % 26));
            column = column / 26 - 1;
        }
        return sb.toString();
    }

    private boolean isContainName(String[] names, String targetName) {

        for (String name : names) {
            if (name.equals(targetName)) return true;
        }
        return false;

    }
}



