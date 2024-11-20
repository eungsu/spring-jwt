package com.example.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.example.model.ERole;

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
	
	public String generateAccessToken(Long userId, ERole role) {
        return createToken(userId, role, ACCESS_SECRET_KEY, EXPIRATION_MS);
    }
	
	public String generateRefreshToken(Long userId) {
		return createToken(userId, REFRESH_SECRET_KEY, REFERSH_EXPIRATION_MS);
	}
	
	public boolean validateAccessToken(String token) {
		return validateToken(token, ACCESS_SECRET_KEY);
	}
	
	public boolean validateRefreshToken(String token) {
		return validateToken(token, REFRESH_SECRET_KEY);
	}
	
	public Long extractUserIdInAccessToken(String accessToken) {
		return extractUsername(accessToken, ACCESS_SECRET_KEY);
	}
	
	public Long extractUserIdInRefreshToken(String refreshTokeon) {
		return extractUsername(refreshTokeon, REFRESH_SECRET_KEY);
	}
	
	private Long extractUsername(String token, SecretKey key) {
		return Long.valueOf(Jwts.parser()
		.verifyWith(key)
		.build()
		.parseSignedClaims(token)
		.getPayload().getSubject());
	}
	
	public ERole extractRoleInAccessToken(String accessToken) {
		return ERole.valueOf(Jwts.parser()
		.verifyWith(ACCESS_SECRET_KEY)
		.build()
		.parseSignedClaims(accessToken)
		.getPayload()
		.get("role", String.class));
	}

	private String createToken(Long userId, ERole role, SecretKey key, int expirationTime) {
		return Jwts.builder()
				.subject(String.valueOf(userId))
				.claim("role", role.name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
	}

	private String createToken(Long userId, SecretKey key, int expirationTime) {
		return Jwts.builder()
				.subject(String.valueOf(userId))
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
