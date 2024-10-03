package com.springmip.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springmip.authorization.service.TokenService;

@RestController
public class Controllers {

	@Autowired
	private TokenService tokenService;

	@GetMapping("token")
	public ResponseEntity<Object> token(
			@RegisteredOAuth2AuthorizedClient("azure") OAuth2AuthorizedClient authorizedClient) throws Exception {
		return tokenService.tokenService(authorizedClient);
	}

}
