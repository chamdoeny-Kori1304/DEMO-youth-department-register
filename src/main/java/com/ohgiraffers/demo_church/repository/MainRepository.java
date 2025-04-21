package com.ohgiraffers.demo_church.repository;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import com.ohgiraffers.demo_church.util.GoogleSheetUtils;
import com.ohgiraffers.demo_church.domain.OrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;


@Slf4j
@Repository
@RequiredArgsConstructor
public class MainRepository {
    private final GoogleSheetConfig googleSheetConfig;
    private final GoogleSheetUtils googleSheetUtils;

    @Value("${google.spreadsheet.id}")
    private String SPREAD_SHEET_ID;

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

    public List<Map<String, String>> findData(String range, String defaultColumnName) {
        List<Map<String, String>> findResponse = new ArrayList<>();
        getDataFromSheet(range, defaultColumnName, (rowMap) -> {
            boolean shouldAdd = googleSheetUtils.shouldAddRowToResponse(rowMap, defaultColumnName);
            if (shouldAdd) {
                findResponse.add(rowMap);
                return true;
            }
            return null; // return null to continue processing
        });
        return findResponse;
    }

    public Map<String, List<String>> getTeamToMembersMap(String range, String defaultColumnName, String yearQuarter) {
        Map<String, List<String>> teamToMembersMap = new HashMap<>();
        getDataFromSheet(range, defaultColumnName, (rowMap) -> {
            String key = rowMap.get(yearQuarter);
            if (!teamToMembersMap.containsKey(key)) {
                teamToMembersMap.put(key, new ArrayList<>());
            }
            teamToMembersMap.get(key).add(rowMap.get("이름"));
            return teamToMembersMap;
        });
        return teamToMembersMap;
    }

    private <T> void getDataFromSheet(String range, String defaultColumnName, Function<Map<String, String>, T> mapper) {
        List<List<Object>> values;
        try{
            Sheets sheets = googleSheetConfig.provideSheetsClient();
            values = googleSheetUtils.filterData( sheets ,range,SPREAD_SHEET_ID);
            if (values == null || values.isEmpty()) {
                return;
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<String> columnNames = googleSheetUtils.getColumnNames(values.get(0));
        List<Map<String, String>> rowMaps = createRowMaps(values, columnNames);
        processRowMaps(rowMaps, defaultColumnName, mapper);
    }

    private List<Map<String, String>> createRowMaps(List<List<Object>> values, List<String> columnNames) {
        List<Map<String, String>> rowMaps = new ArrayList<>();
        for (int i = 1; i < values.size(); i++) {
            List<Object> row = values.get(i);
            rowMaps.add(googleSheetUtils.createRowMapFromSheetData(columnNames, row));
        }
        return rowMaps;
    }

    private <T> void processRowMaps(List<Map<String, String>> rowMaps, String defaultColumnName, Function<Map<String, String>, T> mapper) {
        int emptyRowCount = 0;
        for (Map<String, String> rowMap : rowMaps) {
            if (emptyRowCount >= 2) {
                break;
            }
            T result = mapper.apply(rowMap);
            if (result == null) {
                emptyRowCount++;
            }
        }
    }


}
