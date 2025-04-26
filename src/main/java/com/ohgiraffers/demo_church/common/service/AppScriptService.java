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

//    public AppScriptService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }


//        ResponseEntity<String> response = restClient.getForEntity(url, String.class);
//        return response.getBody();


//    public String triggerDataSorting() {
//        String url = APP_SCRIPT_URL + "?action=sortYouthSheet2Descending";
//
//        String res= restClient.get()
//                .uri(url)
//                .retrieve()
//                .body(String.class);
//        return res;
//
//    }

    public Mono<String> triggerAppScriptWithAuth() throws  GeneralSecurityException {
        try {
            GoogleCredentials credential = GoogleCredentials.fromStream(
                            new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/script.process")); // App Script 실행 권한

//            GoogleCredentials credential = GoogleCredentials.fromStream(
//                            new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
//                    .createScoped("https://www.googleapis.com/auth/spreadsheets");

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

//            Script script = new Script.Builder(NetHttpTransport, )

            ExecutionRequest request = new ExecutionRequest()
                    .setFunction("getFoldersUnderRoot");
            HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credential);
            Script script =
                    new Script.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
                            .setApplicationName("True Youth Department")
                            .build();

            Script.Projects projects = script.projects();

            // Creates a new script project.
            Project createOp = projects.create(new CreateProjectRequest().setTitle("My Script")).execute();

            // Uploads two files to the project.
            File file1 = new File()
                    .setName("hello")
                    .setType("SERVER_JS")
                    .setSource("function helloWorld() {\n  console.log(\"Hello, world!\");\n}");
            File file2 = new File()
                    .setName("appsscript")
                    .setType("JSON")
                    .setSource("{\"timeZone\":\"America/New_York\",\"exceptionLogging\":\"CLOUD\"}");
            Content content = new Content().setFiles(Arrays.asList(file1, file2));
            Content updatedContent = projects.updateContent(createOp.getScriptId(), content).execute();

            // Logs the project URL.
            System.out.printf("https://script.google.com/d/%s/edit\n", updatedContent.getScriptId());


//            script.projects();
//            String tokenValue = credential.getAccessToken().getTokenValue();




//            // WebClient를 사용하여 인증된 요청 보내기
//            WebClient client = WebClient.builder()
//                    .defaultHeader("Authorization", "Bearer " + credential.getAccessToken().getTokenValue())
//                    .build();
//
//            return client.get()
//                    .uri(APP_SCRIPT_URL + "?action=yourFunction") // App Script 웹 앱 URL 및 실행할 함수명
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .doOnSuccess(response -> log.info("App Script Response: {}", response))
//                    .doOnError(error -> log.error("Error calling App Script: {}", error.getMessage()));

            return  Mono.create(sink -> {});
        } catch (IOException e) {
            log.error("Error loading credentials file: {}", e.getMessage());
            return Mono.error(new RuntimeException("Failed to load credentials", e));
        }
    }


//    public String triggerDataSorting() {
//        String url = APP_SCRIPT_URL + "?action=sortYouthSheet2Descending";
//
//        try {
//            String response = restClient.get()
//                    .uri(url)
//                    .retrieve()
//                    .body(String.class);
//
//            log.info("App Script Response: {}", response); // 단순 텍스트 응답 로깅
//            return "App Script 응답: " + response;
//
//        } catch (Exception e) {
//            log.error("App Script 호출 중 오류 발생: {}", e.getMessage());
//            return "App Script 호출 중 오류 발생: " + e.getMessage();
//        }
//    }

    // 필요하다면 POST 요청을 보내는 메서드도 추가할 수 있습니다.
    // 예: 정렬 기준을 POST 데이터로 전달
    // public String triggerDataSortingWithCriteria(String sortColumn) {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    //
    //     MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
    //     map.add("action", "데이터정렬");
    //     map.add("sortColumn", sortColumn);
    //
    //     HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
    //
    //     ResponseEntity<String> response = restTemplate.postForEntity(appScriptUrl, request, String.class);
    //     return response.getBody();
    // }


}

