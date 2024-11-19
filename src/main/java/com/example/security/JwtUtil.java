package com.example.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String ACCESS_SECRET = "tvBOmQXZMS1U4je7lM7tI0vIbBPszz13NahgFFe8yx0gjz8HwqpPhUMbvRuATTvXmbvMFHVmbqc3tvl3Rn3JMA4JcnjsTei4";
	private static final String REFRESH_SECRET = "g4mJNmmPSWkMJqZi1VScYyW8S9uZj5BbpcqeolbzkRvrNObO21P5qTIIloU1X1ZB7j7wLrMPxgzTCAfNi6nN5qrTBUB0ZV9d";	
	private static final int EXPIRATION_MS = 1000 * 60 * 15;
	private static final int REFERSH_EXPIRATION_MS = 1000 * 60 * 60 * 24;
	
	private final SecretKey ACCESS_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(ACCESS_SECRET));
	private final SecretKey REFRESH_SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(REFRESH_SECRET));
	
	public String generateAccessToken(String username) {
        return createToken(username, ACCESS_SECRET_KEY, EXPIRATION_MS);
    }
	
	public String generateRefreshToken(String username) {
		return createToken(username, REFRESH_SECRET_KEY, REFERSH_EXPIRATION_MS);
	}
	
	public boolean validateAccessToken(String token) {
		return validateToken(token, ACCESS_SECRET_KEY);
	}
	
	public boolean validateRefreshToken(String token) {
		return validateToken(token, REFRESH_SECRET_KEY);
	}
	
	public String extractUsernameInAccessToken(String accessToken) {
		return extractUsername(accessToken, ACCESS_SECRET_KEY);
	}
	
	public String extractUsernameInRefreshToken(String refreshTokeon) {
		return extractUsername(refreshTokeon, REFRESH_SECRET_KEY);
	}
	
	private String extractUsername(String token, SecretKey key) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload().getSubject();
	}
		
	private String createToken(String username, SecretKey key, int expirationTime) {
		return Jwts.builder()
				.subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
	}
		
	private boolean validateToken(String token, SecretKey key) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
