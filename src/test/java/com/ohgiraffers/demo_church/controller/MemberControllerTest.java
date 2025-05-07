package com.ohgiraffers.demo_church.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohgiraffers.demo_church.domain.Member;
import com.ohgiraffers.demo_church.domain.enums.Gender;
import com.ohgiraffers.demo_church.dto.MemberDTO;

import com.ohgiraffers.demo_church.util.ReflectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Controller test는 실제 sheet를 이용해서 구현, 로직에 동작보다는
/* 테스트 하고 싶은것
  1. 실제 Member sheet의 colum 개수와 class Member의 필드 개수
  2. Validation의 응답이 적절한 형태의 Json으로 반환되고 있는가?
**/

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MemberControllerTest {
    MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext wac;

    static MemberDTO memberDTO1 = new MemberDTO("홍길동", Gender.MALE, "010-1111-2222", "서울시 강남구", LocalDate.of(1990, 1, 1), "철수 친구", "개발팀");

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }


    @Test
    void getSheetData_ShouldReturnData() throws Exception {
        // given
        MvcResult mvcResult = mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andReturn();

        // when
        String responseBody = mvcResult.getResponse().getContentAsString();
        List<Map<String, String>> data = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, String>>>() {
        });


        // then
        int fieldCount = ReflectionUtil.getFieldCount(Member.class);

        // 메모와 출석률은 Member class 포함 안 함
        assertThat(data.get(0)).hasSize(fieldCount + 2);

    }

    @Test
    void getCurrentTeamMembers_ShouldReturnTeamData() throws Exception {
        // given
        List<Map<String, String>> dummyData = List.of(
                Map.of("이름", "홍길동"),
                Map.of("이름", "김영희")
        );


        // when & then
        MvcResult mvcResult = mockMvc.perform(get("/team-members"))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString();

        Map<String, List<String>> data = objectMapper.readValue(responseBody, new TypeReference<Map<String, List<String>>>() {
        });

        //TODO 실제로 저장된 셀 명단을 확인하고 비교하자
        assertThat(data).hasSize(5 + 1 + 1); // 리더는 5명 + 장결자팀 + 아무것도 아닌 팀
    }

    @Test
    void postMembers_shouldSaveMemberSuccessfully() throws Exception {
        // given
        String regex = "^[a-zA-Z0-9_]+![A-Z]+\\d+:[A-Z]+\\d+$";

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());//추가

        // when & then
        MvcResult mvcResult = mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(memberDTO1)))
                .andExpect(status().isCreated())
                .andExpect(content().string(matchesPattern(regex)))
                .andReturn();

    }

}