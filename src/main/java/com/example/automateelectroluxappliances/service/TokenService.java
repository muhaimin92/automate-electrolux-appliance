package com.example.automateelectroluxappliances.service;

import com.example.automateelectroluxappliances.model.RefreshTokenRequest;
import com.example.automateelectroluxappliances.model.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class TokenService {

    private final WebClient.Builder webClientBuilder;

    @Value("${api.base-url}")
    private String baseUrl;

    @Value("${api.api-key}")
    private String apiKey;

    private String accessToken;
    private String refreshToken;

    @Autowired
    public TokenService(WebClient.Builder webClientBuilder,
                        @Value("${api.api-key}") String apiKey,
                        @Value("${api.access-token}") String initialAccessToken,
                        @Value("${api.refresh-token}") String initialRefreshToken) {
        this.webClientBuilder = webClientBuilder;
        this.apiKey = apiKey;
        this.accessToken = initialAccessToken;
        this.refreshToken = initialRefreshToken;
    }

    public Mono<String> getAccessToken() {
        if (accessToken == null) {
            return retrieveAccessToken();
        } else {
            return Mono.just(accessToken);
        }
    }

    public void clear(){
        accessToken = null;
    }

    private Mono<String> retrieveAccessToken() {
        String tokenUrl = baseUrl + "/api/v1/token/refresh";
        return webClientBuilder.build()
                .post()
                .uri(tokenUrl)
                .body(BodyInserters.fromValue(new RefreshTokenRequest(refreshToken)))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(tokenResponse -> {
                    accessToken = tokenResponse.getAccessToken();
                    if (tokenResponse.getRefreshToken() != null) {
                        refreshToken = tokenResponse.getRefreshToken();
                    }
                })
                .map(TokenResponse::getAccessToken).onErrorResume(WebClientResponseException.class, e -> {
                    clear();
                    return Mono.error(e);
                });
    }

}
