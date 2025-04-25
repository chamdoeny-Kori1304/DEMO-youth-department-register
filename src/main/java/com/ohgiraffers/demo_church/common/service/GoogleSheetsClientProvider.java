package com.ohgiraffers.demo_church.common.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;


@Service
public class GoogleSheetsClientProvider {
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/google_api_key.json";

    private Sheets sheets;

    public Sheets getClient() {
        if (sheets == null) {
            try {
                GoogleCredentials credential = GoogleCredentials.fromStream(
                                new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                        .createScoped("https://www.googleapis.com/auth/spreadsheets");

                sheets = new Sheets.Builder(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        JSON_FACTORY,
                        new HttpCredentialsAdapter(credential)
                ).setApplicationName("uniflee").build();
            } catch (IOException | GeneralSecurityException e) {
                throw new RuntimeException("Failed to create Google Sheets client", e);
            }
        }
        return sheets;
    }
}
