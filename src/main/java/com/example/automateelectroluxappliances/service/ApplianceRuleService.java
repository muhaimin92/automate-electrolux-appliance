package com.example.automateelectroluxappliances.service;

import com.example.automateelectroluxappliances.model.ApplianceRule;
import com.example.automateelectroluxappliances.repository.ApplianceRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ApplianceRuleService {

    private ApplianceRuleRepository applianceRuleRepository;

    private ApplianceRuleEngine applianceRuleEngine;

    private ApplianceService applianceService;

    @Autowired
    public ApplianceRuleService(ApplianceRuleRepository applianceRuleRepository, ApplianceRuleEngine applianceRuleEngine, ApplianceService applianceService) {
        this.applianceRuleRepository = applianceRuleRepository;
        this.applianceRuleEngine = applianceRuleEngine;
        this.applianceService = applianceService;
    }

    public Mono<ApplianceRule> saveRule(ApplianceRule applianceRule) {
        applianceRuleEngine.validateRule(applianceRule);
        return applianceRuleRepository.save(applianceRule);
    }

    public Flux<ApplianceRule> getAllRules() {
        return applianceRuleRepository.findAll();
    }

    public Mono<Void> deleteRule(String id) {
        return applianceRuleRepository.deleteById(id);
    }

    public void evaluateRules() {
        getAllRules()
                .flatMap(rule -> applianceService.getApplianceState(rule.getApplianceId())
                        .filter(state -> applianceRuleEngine.evaluateCondition(rule.getCondition(), state))
                        .flatMapMany(state -> Flux.fromIterable(rule.getAction())
                                .flatMap(action -> applianceService.sendCommand(rule.getApplianceId(), action.getExecuteCommand()))
                        )
                ).subscribe();
    }

}