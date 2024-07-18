package com.example.automateelectroluxappliances.controller;

import com.example.automateelectroluxappliances.model.ApplianceRule;
import com.example.automateelectroluxappliances.model.ResponseError;
import com.example.automateelectroluxappliances.service.ApplianceRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class ApplianceRuleController {

    @Autowired
    private ApplianceRuleService applianceRuleService;

    @PostMapping("/rules")
    public Mono<ResponseEntity<Object>> createRule(@RequestBody ApplianceRule applianceRule) {
        try {
            return applianceRuleService.saveRule(applianceRule)
                    .map(savedRule -> new ResponseEntity<>(savedRule, HttpStatus.CREATED));
        } catch (IllegalArgumentException e) {
            return Mono.just(new ResponseEntity<>(new ResponseError(e.getMessage()), HttpStatus.BAD_REQUEST));
        }
    }

    @GetMapping("/rules")
    public Flux<ApplianceRule> getAllRules() {
        return applianceRuleService.getAllRules();
    }

    @DeleteMapping("/rules/{id}")
    public Mono<ResponseEntity<Void>> deleteRule(@PathVariable String id) {
        return applianceRuleService.deleteRule(id)
                .then(Mono.fromCallable(() -> ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}