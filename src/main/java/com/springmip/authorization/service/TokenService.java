package com.springmip.authorization.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface TokenService {
	ResponseEntity<Object> tokenService(OAuth2AuthorizedClient authorizedClient) throws Exception;
}
