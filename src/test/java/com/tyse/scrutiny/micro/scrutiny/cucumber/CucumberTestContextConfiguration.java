package com.tyse.scrutiny.micro.scrutiny.cucumber;

import com.tyse.scrutiny.micro.scrutiny.IntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@IntegrationTest
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
