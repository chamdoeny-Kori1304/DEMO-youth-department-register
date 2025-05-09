package com.ohgiraffers.demo_church.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.common.SheetResponse;
import com.ohgiraffers.demo_church.common.service.GoogleSheetsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class AttendanceRepository {

    private final GoogleSheetsService googleSheetsService;

    @Value("${google.spreadsheet.Attendance}")
    private String sheetName;

    @Getter
    @Value("${google.spreadsheet.Attendance}!${google.spreadsheet.Attendance.range}")
    private String sheetRange;

    // TODO 구글시트에 이름을 사전순으로 정렬하고 targetNames도 사전순으로 정렬해서 하면 O(n)으로 끝낼 수 있다.
    public SheetResponse updateAttendance(String[] targetNames, String targetDate) {
        List<List<Object>> values;
        Arrays.sort(targetNames);

        try {
            values = googleSheetsService.getSheetData(sheetRange);

            if (values == null || values.isEmpty()) return null;

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

            BatchUpdateValuesResponse response = googleSheetsService.updateSheetData(body);

            List<String> cellList = new ArrayList<>();
            response.values().forEach(value -> {
                cellList.add(value.toString());
            });

            SheetResponse res = new SheetResponse(true, "Success update", cellList.toArray(new String[0]));

            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    public List<String> getNamesByDate(String targetDate) {
        List<List<Object>> values;
        List<String> response = new ArrayList<>();

        try {
            values = googleSheetsService.getSheetData(sheetRange);

            if (values == null || values.isEmpty()) return response;

            List<Object> header = values.get(0);
            int colIndex = header.indexOf(targetDate);

            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);
                if (!row.isEmpty() && row.get(colIndex).toString().equals("O")) {
                    response.add(row.get(0).toString());
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return response;

    }

    public void addRow(List<Object> rowData) throws IOException, GeneralSecurityException {

        ValueRange body = new ValueRange().setValues(Collections.singletonList(rowData));
        String DEFAULT_VALUE = "null";

        // 이름& 번호 시트 개수는 미포함
        while (rowData.size() < 54+2) {
            rowData.add(DEFAULT_VALUE);
        }
        googleSheetsService.appendSheetData(sheetRange, body);

        log.info("새로운 행이 스프레드시트에 추가되었습니다: {} ", rowData);
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



