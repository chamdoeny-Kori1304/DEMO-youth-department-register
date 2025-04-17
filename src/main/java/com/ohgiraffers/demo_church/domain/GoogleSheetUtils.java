package com.ohgiraffers.demo_church.domain;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoogleSheetUtils {

    public List<String> getColumnNames(List<Object> sheetHeader) {
        List<String> columNames = new ArrayList<>();

        for (Object columName : sheetHeader) {
            columNames.add(columName.toString());
        }
        return columNames;
    }

    public Map<String, String> createRowMapFromSheetData(List<String> columnNames, List<Object> row) {
        Map<String, String> rowMap = new HashMap<>();
        for (int j = 0; j < columnNames.size(); j++) {
            // row.size()는 columnNames보다 작을 수 있다.
            // 스프레드시트 데이터를 가져올 때 비어있다면 더 이상 가져오지 않는다.
            String value = j < row.size() ? row.get(j).toString() : "";
            rowMap.put(columnNames.get(j), value);
        }
        return rowMap;
    }

    public boolean shouldAddRowToResponse(Map<String, String> rowMap, String defaultColumnName) {
        return (defaultColumnName == null || defaultColumnName.isEmpty()) ||
                (rowMap.get(defaultColumnName) != null && !rowMap.get(defaultColumnName).isEmpty());
    }
}
