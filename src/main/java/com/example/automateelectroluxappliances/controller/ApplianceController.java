package com.example.automateelectroluxappliances.controller;

import com.example.automateelectroluxappliances.model.Appliance;
import com.example.automateelectroluxappliances.model.ApplianceState;
import com.example.automateelectroluxappliances.service.ApplianceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApplianceController {

    @Autowired
    private ApplianceService applianceService;

    @GetMapping("/appliances")
    public Mono<List<Appliance>> getUserAppliances() {
        return applianceService.getAllAppliances();
    }

    @GetMapping("/appliances/{applianceId}/state")
    public Mono<ApplianceState> getApplianceState(@PathVariable String applianceId) {
        return applianceService.getApplianceState(applianceId);
    }

    @PutMapping("/{applianceId}/command")
    public Mono<ResponseEntity<Void>> sendCommandToAppliance(@PathVariable String applianceId, @RequestBody Map<String, Object> command) {
        return applianceService.sendCommand(applianceId, command)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }


}
