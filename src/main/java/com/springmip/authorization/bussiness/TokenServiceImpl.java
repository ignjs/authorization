package com.springmip.authorization.bussiness;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springmip.authorization.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper; // Jackson ObjectMapper

	public ResponseEntity<Object> tokenService(OAuth2AuthorizedClient authorizedClient) throws Exception {
		Map<String, Object> responseMap = new HashMap<>();

		// Add token details
		responseMap.put("token", authorizedClient.getAccessToken().getTokenValue());
		responseMap.put("refreshToken",
				authorizedClient.getRefreshToken() != null ? authorizedClient.getRefreshToken().getTokenValue() : null);

		// Get user details
		String userInfoEndpoint = "https://graph.microsoft.com/v1.0/me"; // Microsoft Graph API user info endpoint
		String accessToken = authorizedClient.getAccessToken().getTokenValue();

		// Set the Authorization header
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, entity, String.class);

		// Add user details to response map
		if (response.getStatusCode() == HttpStatus.OK) {
			try {
				Map<String, Object> userDetails = objectMapper.readValue(response.getBody(), Map.class);
				responseMap.put("userDetails", userDetails);
				return ResponseEntity.ok(responseMap); // Return 200 OK with user details
			} catch (JsonProcessingException e) {
				responseMap.put("error", "Error processing user details");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
			}
		} else {
			responseMap.put("error", "Error retrieving user details");
			return ResponseEntity.status(response.getStatusCode()).body(responseMap); // Return error status and message
		}
	}
}
