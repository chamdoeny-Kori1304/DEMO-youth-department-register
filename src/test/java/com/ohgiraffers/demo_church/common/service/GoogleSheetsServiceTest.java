package com.ohgiraffers.demo_church.common.service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.ohgiraffers.demo_church.config.GoogleSheetConfig;
import com.ohgiraffers.demo_church.util.GoogleSheetUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class GoogleSheetsServiceTest {

    @Autowired
    private GoogleSheetsService googleSheetsService;

    @MockitoBean
    private GoogleSheetConfig googleSheetConfig;

    @MockitoBean
    private GoogleSheetUtils googleSheetUtils;

    @Test
    void shouldGetSheetData() throws GeneralSecurityException, IOException {
        // Arrange
        String range = "A1:B10";
        List<List<Object>> expectedData = new ArrayList<>();
        expectedData.add(List.of("Name", "Age"));
        expectedData.add(List.of("John Doe", "30"));
        expectedData.add(List.of("Jane Doe", "25"));

        Sheets mockSheets = Mockito.mock(Sheets.class);
        when(googleSheetConfig.provideSheetsClient()).thenReturn(mockSheets);
        when(googleSheetUtils.filterData(any(Sheets.class), anyString(), anyString())).thenReturn(expectedData);

        // Act
        List<List<Object>> result = googleSheetsService.getSheetData(range);

        // Assert
        assertEquals(expectedData, result);
    }

    @Test
    void shouldUpdateSheetData() throws IOException, GeneralSecurityException {
        // Arrange
        String spreadsheetId = "test-spreadsheet-id";
        BatchUpdateValuesRequest body = new BatchUpdateValuesRequest();

        when(googleSheetConfig.provideSheetsClient()).thenReturn(new Sheets.Builder(null, null, null).build());

        // Act
        googleSheetsService.updateSheetData(spreadsheetId, body);

        // Assert
        // 메서드가 정상적으로 실행되면 테스트 통과
    }

//    @Test
//    void shouldAppendSheetData() throws IOException, GeneralSecurityException {
//        // Arrange
//        String spreadsheetId = "test-spreadsheet-id";
//        String range = "A1:B1";
//        ValueRange body = new ValueRange();
//
//        Sheets.Spreadsheets.Values.Append appendRequest = new Sheets.Spreadsheets.Values.Append(null, null, null);
//        AppendValuesResponse expectedResponse = new AppendValuesResponse();
//        when(googleSheetConfig.provideSheetsClient()).thenReturn(new Sheets.Builder(null, null, null).build());
//        when(appendRequest.setValueInputOption(anyString())).thenReturn(appendRequest);
//        when(appendRequest.execute()).thenReturn(expectedResponse);
//
//        // Act
//        AppendValuesResponse result = googleSheetsService.appendSheetData(range, body);
//
//        // Assert
//        assertEquals(expectedResponse, result);
//    }

    @Test
    void shouldAppendSheetData() throws IOException, GeneralSecurityException {
        // Arrange
        String range = "A1:B1";
        ValueRange body = new ValueRange();

        Sheets mockSheets = Mockito.mock(Sheets.class);
        Sheets.Spreadsheets.Values mockValues = Mockito.mock(Sheets.Spreadsheets.Values.class);
        Sheets.Spreadsheets.Values.Append mockAppend = Mockito.mock(Sheets.Spreadsheets.Values.Append.class);
        AppendValuesResponse expectedResponse = new AppendValuesResponse();

        when(googleSheetConfig.provideSheetsClient()).thenReturn(mockSheets);
        when(mockSheets.spreadsheets()).thenReturn(Mockito.mock(Sheets.Spreadsheets.class));
        when(mockSheets.spreadsheets().values()).thenReturn(mockValues);
        when(mockValues.append(anyString(), anyString(), any(ValueRange.class))).thenReturn(mockAppend);
        when(mockAppend.setValueInputOption(anyString())).thenReturn(mockAppend);
        when(mockAppend.execute()).thenReturn(expectedResponse);

        // Act
        AppendValuesResponse result = googleSheetsService.appendSheetData( range, body);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void shouldFindData() throws GeneralSecurityException, IOException {
        // Arrange
        String range = "A1:B10";
        String defaultColumnName = "Name";
        List<List<Object>> sheetData = new ArrayList<>();
        sheetData.add(List.of("Name", "Age"));
        sheetData.add(List.of("John Doe", "30"));
        sheetData.add(List.of("Jane Doe", "25"));
        sheetData.add(List.of("", ""));

        List<Map<String, String>> expectedResult = new ArrayList<>();
        Map<String, String> row1 = new HashMap<>();
        row1.put("Name", "John Doe");
        row1.put("Age", "30");
        Map<String, String> row2 = new HashMap<>();
        row2.put("Name", "Jane Doe");
        row2.put("Age", "25");
        expectedResult.add(row1);
        expectedResult.add(row2);

        when(googleSheetConfig.provideSheetsClient()).thenReturn(new Sheets.Builder(null, null, null).build());
        when(googleSheetUtils.filterData(any(Sheets.class), anyString(), anyString())).thenReturn(sheetData);
        when(googleSheetUtils.getColumnNames(any())).thenReturn(List.of("Name", "Age"));
        when(googleSheetUtils.createRowMapFromSheetData(any(), any())).thenAnswer(
                invocation -> {
                    List<String> columnNames = invocation.getArgument(0);
                    List<Object> row = invocation.getArgument(1);
                    Map<String, String> rowMap = new HashMap<>();
                    for (int i = 0; i < columnNames.size(); i++) {
                        rowMap.put(columnNames.get(i), row.get(i).toString());
                    }
                    return rowMap;
                }
        );
        when(googleSheetUtils.shouldAddRowToResponse(any(), anyString())).thenReturn(true);

        // Act
        List<Map<String, String>> result = googleSheetsService.findData(range, defaultColumnName);

        // Assert
        assertEquals(expectedResult, result);
    }
}