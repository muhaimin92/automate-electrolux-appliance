package com.example.automateelectroluxappliances.config;

import com.example.automateelectroluxappliances.service.ApplianceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private ApplianceRuleService applianceRuleService;

    @Scheduled(fixedRate = 60000)
    public void scheduleRuleEvaluation() {
        applianceRuleService.evaluateRules();
    }
}
