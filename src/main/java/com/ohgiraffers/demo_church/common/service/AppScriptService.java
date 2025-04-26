package com.ohgiraffers.demo_church.common.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.Content;
import com.google.api.services.script.model.CreateProjectRequest;
import com.google.api.services.script.model.File;
import com.google.api.services.script.model.Project;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

//import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;


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




    public Mono<String> useWebClient() {
        try {
            GoogleCredentials credential = getGoogleCredentials();

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

    public String useScript(){
        try{
            GoogleCredentials credential = getGoogleCredentials();

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Script service =
                    new Script.Builder(HTTP_TRANSPORT, JSON_FACTORY,  new HttpCredentialsAdapter(credential) )
                            .setApplicationName("uniflee").build();
            Script.Projects projects = service.projects();

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return  "";
    }


    private GoogleCredentials getGoogleCredentials() throws IOException
        {
             return GoogleCredentials.fromStream(
                            new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream())
                    .createScoped(Collections.singletonList("https://www.googleapis.com/auth/script.process")); // App Script 실행 권한

        }
}

