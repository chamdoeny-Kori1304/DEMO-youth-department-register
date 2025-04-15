package com.ohgiraffers.demo_church.service;

import com.ohgiraffers.demo_church.domain.OrderInfo;
import com.ohgiraffers.demo_church.repository.MainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {

    private final MainRepository repository;

    public void writeToSheet(String spreadSheetId, String range, OrderInfo orderInfo) {
        repository.save(spreadSheetId, range, orderInfo);

    }

    public List<Map<String, String>> readFromSheet(String spreadSheetId, String range) {
        return  repository.findAll(spreadSheetId, range);

    }

}