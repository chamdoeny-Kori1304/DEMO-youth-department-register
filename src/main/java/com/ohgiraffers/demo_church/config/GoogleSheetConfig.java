package com.ohgiraffers.demo_church.config;


import com.google.api.services.sheets.v4.Sheets;

import com.ohgiraffers.demo_church.common.service.GoogleSheetsClientProvider;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GoogleSheetConfig {
    private final GoogleSheetsClientProvider googleSheetsClientProvider;

    public GoogleSheetConfig(GoogleSheetsClientProvider googleSheetsClientProvider) {
        this.googleSheetsClientProvider = googleSheetsClientProvider;
    }

    public Sheets provideSheetsClient() {
        return googleSheetsClientProvider.getClient();
    }
}
