package com.example.automateelectroluxappliances.service;

import com.example.automateelectroluxappliances.model.Appliance;
import com.example.automateelectroluxappliances.model.ApplianceState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class ApplianceService {

    private final TokenService tokenService;
    private final WebClient.Builder webClientBuilder;

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.api-key}")
    private String apiKey;

    @Autowired
    public ApplianceService(TokenService tokenService, WebClient.Builder webClientBuilder) {
        this.tokenService = tokenService;
        this.webClientBuilder = webClientBuilder.baseUrl(baseUrl);
    }

    public Mono<List<Appliance>> getAllAppliances() {
        return tokenService.getAccessToken()
                .flatMap(accessToken -> makeApiCall(accessToken)
                        .onErrorResume(WebClientResponseException.class, e -> {
                            if (e.getStatusCode().value() == 401) {
                                tokenService.clear();
                                return tokenService.getAccessToken()
                                        .flatMap(newAccessToken -> makeApiCall(newAccessToken));
                            } else {
                                return Mono.error(e);
                            }
                        }));
    }

    public Mono<ApplianceState> getApplianceState(String applianceId) {
        return tokenService.getAccessToken()
                .flatMap(accessToken -> makeApiCall(accessToken,applianceId)
                        .onErrorResume(WebClientResponseException.class, e -> {
                            if (e.getStatusCode().value() == 401) {
                                tokenService.clear();
                                return tokenService.getAccessToken()
                                        .flatMap(newAccessToken -> makeApiCall(accessToken,applianceId));
                            } else {
                                return Mono.error(e);
                            }
                        }));
    }

    public Mono<Void> sendCommand(String applianceId, Map<String, Object> command) {
        System.out.println("sending command ---> "+ command.toString());
        return tokenService.getAccessToken()
                .flatMap(accessToken -> sendCommandToAppliance(accessToken,applianceId, command)
                        .onErrorResume(WebClientResponseException.class, e -> {
                            if (e.getStatusCode().value() == 401) {
                                tokenService.clear();
                                return tokenService.getAccessToken()
                                        .flatMap(newAccessToken -> sendCommandToAppliance(accessToken,applianceId, command));
                            } else {
                                return Mono.error(e);
                            }
                        }));
    }

    private Mono<List<Appliance>> makeApiCall(String accessToken) {
        return webClientBuilder.build()
                .get()
                .uri(baseUrl + "/api/v1/appliances")
                .header("Authorization", "Bearer " + accessToken)
                .header("x-api-key", apiKey)
                .retrieve()
                .bodyToFlux(Appliance.class)
                .collectList();
    }

    private Mono<ApplianceState> makeApiCall(String accessToken ,String applianceId) {
        String uri = baseUrl +  String.format("/api/v1/appliances/%s/state", applianceId);
        return webClientBuilder.build()
                .get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .header("x-api-key", apiKey)
                .retrieve()
                .bodyToMono(ApplianceState.class);
    }

    private Mono<Void> sendCommandToAppliance(String accessToken, String applianceId, Map<String, Object> command) {
        String uri = baseUrl + String.format("/api/v1/appliances/%s/command", applianceId);
        return webClientBuilder.build()
                .put()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .header("x-api-key", apiKey)
                .bodyValue(command)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
