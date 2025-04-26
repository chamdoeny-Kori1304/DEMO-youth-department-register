package com.ohgiraffers.demo_church.common.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.*;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;


// ID : AKfycbwzdoifRnysrkxzI6NDOTsZhhP32_0VQyQ_69Jh96vSU8N8RdKnih8hEWPn-vHwy78i
// web URL: https://script.google.com/macros/s/AKfycbwzdoifRnysrkxzI6NDOTsZhhP32_0VQyQ_69Jh96vSU8N8RdKnih8hEWPn-vHwy78i/exec
@Slf4j
@Service
@RequiredArgsConstructor
public class AppScriptService {

//    @Autowired
//     private RestTemplate restTemplate;

//    final RestClientConfig restClientConfig;
//    private final RestClient restClient;


    //    @Value("${google.apps.script.web.app.url}")
//    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    private String APP_SCRIPT_URL = "https://script.google.com/macros/s/AKfycbwA1SJUElGyXxUFD31Ccv5f7CpfCWnRCCLn5heCkJVwuT59DJr7xQxyADlRre0sJPvz/exec";
    private static final String CREDENTIALS_FILE_PATH = "/google_api_key.json";
    private String SCRIPT_ID="1BJF6dTj-8KmW_-qdgVTTJiQJPzxsmPRrMjski7JvZlSdAr5WHA_o4v04";
    private String SCRIPT_ID1= "AKfycbwA1SJUElGyXxUFD31Ccv5f7CpfCWnRCCLn5heCkJVwuT59DJr7xQxyADlRre0sJPvz";

    public Mono<String> triggerAppScriptWithAuth() {
        try {
            GoogleCredentials credential = GoogleCredentials.fromStream(
                            new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/script.process")); // App Script 실행 권한

            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);

            // WebClient를 사용하여 인증된 요청 보내기
            WebClient client = WebClient.builder()
                    .defaultHeader("Authorization", "Bearer " + credential.getAccessToken().getTokenValue())
                    .build();

            return client.get()
                    .uri(APP_SCRIPT_URL + "?action=yourFunction") // App Script 웹 앱 URL 및 실행할 함수명
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(response -> log.info("App Script Response: {}", response))
                    .doOnError(error -> log.error("Error calling App Script: {}", error.getMessage()));

        } catch (IOException e) {
            log.error("Error loading credentials file: {}", e.getMessage());
            return Mono.error(new RuntimeException("Failed to load credentials", e));
        }
    }
}

