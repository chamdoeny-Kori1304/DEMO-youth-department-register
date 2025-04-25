package com.ohgiraffers.demo_church.common.service;


import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import com.ohgiraffers.demo_church.util.GoogleSheetUtils;

import org.springframework.beans.factory.annotation.Value;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    @Value("${google.spreadsheet.id}")
    private String SPREAD_SHEET_ID;

    // TODO GoogleSheetConfig & GoogleSheetUtils s 추가
    private final GoogleSheetConfig googleSheetConfig;
    private final GoogleSheetUtils googleSheetUtils;

    public List<List<Object>> getSheetData(String range) throws GeneralSecurityException, IOException {
        Sheets sheets = googleSheetConfig.provideSheetsClient();
        return googleSheetUtils.filterData(sheets, range, SPREAD_SHEET_ID);
    }

    public void updateSheetData(String spreadsheetId, BatchUpdateValuesRequest body) throws IOException, GeneralSecurityException {
        Sheets sheets = googleSheetConfig.provideSheetsClient();
        sheets.spreadsheets().values()
                .batchUpdate(spreadsheetId, body)
                .execute();
    }

    public AppendValuesResponse appendSheetData( String range, ValueRange body) throws IOException, GeneralSecurityException {
        Sheets sheets = googleSheetConfig.provideSheetsClient();
        AppendValuesResponse res = sheets.spreadsheets().values()
                .append(SPREAD_SHEET_ID, range, body)
                .setValueInputOption("RAW")
                .execute();

        return res;

    }

    public List<Map<String, String>> findData(String range, String defaultColumnName) throws GeneralSecurityException, IOException {
        List<Map<String, String>> findResponse = new ArrayList<>();

//         = sheetsService.getSheetData(range, defaultColumnName);
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


    public  <T> void getDataFromSheet(String range, String defaultColumnName, Function<Map<String, String>, T> mapper) {
        List<List<Object>> values;
        try {
            Sheets sheets = googleSheetConfig.provideSheetsClient();
            values = googleSheetUtils.filterData(sheets, range, SPREAD_SHEET_ID);
            if (values == null || values.isEmpty()) {
                return;
            }
        } catch (Exception e) {
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
