package com.ohgiraffers.demo_church.common.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppScriptServiceTest {
    @Autowired
    private AppScriptService service;

    @Test
    void TestGetAppScripts() throws GeneralSecurityException {
       service.triggerAppScriptWithAuth();
    }
}