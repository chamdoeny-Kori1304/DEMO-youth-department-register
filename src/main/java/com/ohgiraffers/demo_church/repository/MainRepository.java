package com.ohgiraffers.demo_church.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import com.ohgiraffers.demo_church.domain.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MainRepository {

    private final GoogleSheetConfig googleSheetConfig;

    @Value("${google.spreadsheet.id}")  // <-- 이렇게 수정해야 동작함
    private String SPREAD_SHEET_ID;

    int emptyRowCount = 0;
    private List<Map<String, String>> findResponse = new ArrayList<>();


    public void save(String spreadsheetId, String range, OrderInfo request) {
        try {
            List<List<Object>> values = List.of(
                    List.of(
                            request.getId().toString(),
                            "미수령",
                            request.getOrderDate().toString(),
                            request.getItemId().toString()
                    )
            );
            Sheets service = googleSheetConfig.provideSheetsClient();
            ValueRange body = new ValueRange().setValues(values);
            service.spreadsheets().values()
                    .append(spreadsheetId, range, body)
                    .setValueInputOption("USER_ENTERED")
                    .setIncludeValuesInResponse(true)
                    .execute();
        } catch (Exception e) {
            log.error("Failed to write data to the spreadsheet", e);
            throw new RuntimeException("Failed to write data to the spreadsheet: " + e.getMessage(), e);
        }
    }

    public List<Map<String, String>> findData(String range) {

        try {
            List<List<Object>> values = this.filterData(range);
            if (values == null || values.isEmpty()) return Collections.emptyList();
            List<String> columnNames = getColumNames(values.get(0));


            for (int i = 1; i < values.size(); i++) {
                List<Object> row = values.get(i);

                if (emptyRowCount >= 2) {
                    emptyRowCount = 0;
                    break;
                }

                this.addRowOnFindResponse(columnNames, row, "이름");

            }

            return findResponse;
        } catch (Exception e) {
            log.error("Failed to read data from the spreadsheet", e);
            throw new RuntimeException("Failed to read data: " + e.getMessage(), e);
        }
    }

    private List<List<Object>> filterData(String range) {

        try {
            Sheets service = googleSheetConfig.provideSheetsClient();
            ValueRange response = service.spreadsheets().values()
                    .get(SPREAD_SHEET_ID, range)
                    .execute();

            List<List<Object>> values = response.getValues();
            return values;
        } catch (Exception e) {
            log.error("Failed to filterData data from the spreadsheet", e);
            throw new RuntimeException("Failed to filterData data: " + e.getMessage(), e);

        }


    }

    private List<String> getColumNames(List<Object> sheetHeader) {
        List<String> columNames = new ArrayList<>();

        for (Object columName : sheetHeader) {
            columNames.add(columName.toString());
        }
        return columNames;
    }

    // TODO "이름"이라는 고정된 값이 아닌 특정한 것으로 넣자
    private void addRowOnFindResponse(List<String> columnNames, List<Object> row, String defaultColumName) {
        int rowSize = row.size();
        Map<String, String> map = new HashMap<>();
        for (int j = 0; j < columnNames.size(); j++) {

            // row.size()는 columNames보다 작을 수 있다. // sheet data를 가져올때 비어있다면 더 이상 가져오지 않는다.
            String value = rowSize > j ? row.get(j).toString() : "";
            map.put(columnNames.get(j), value);
        }
        if (map.get(defaultColumName) != null && !map.get(defaultColumName).isEmpty()) {

            findResponse.add(map);
        } else emptyRowCount++;
    }

}
