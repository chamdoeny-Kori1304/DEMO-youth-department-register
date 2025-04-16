package com.ohgiraffers.demo_church.service;

import com.ohgiraffers.demo_church.domain.OrderInfo;
import com.ohgiraffers.demo_church.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final MainRepository repository;

    @Value("${google.spreadsheet.main}!${google.spreadsheet.main.range}")
    private String ALL_MEMBER_RANGE;

    public void writeToSheet(String spreadSheetId, String range, OrderInfo orderInfo) {
        repository.save(spreadSheetId, range, orderInfo);

    }

    /**
     * 지정된 범위의 Sheet 데이터를 읽어옵니다.
     *
     * @param range 읽어올  Sheet의 범위 (예: "Sheet1!A1:D100")
     * @return 해당 범위의 데이터를 Map 리스트 형식으로 반환
     */
       public List<Map<String, String>> readFromSheet(String range ,String defaultColumName) {

        return  repository.findData(range, defaultColumName);

    }

    /**
     * 모든 회원 데이터를 Main Sheet에서 읽어옵니다.
     *
     * @return 회원 데이터 전체 리스트
     */
    public List<Map<String, String>> readAllMemberFromMainSheet( ) {
        final String DEFAULT_COLUMN_NAME = "이름" ;
        return  repository.findData(ALL_MEMBER_RANGE, DEFAULT_COLUMN_NAME);

    }


}