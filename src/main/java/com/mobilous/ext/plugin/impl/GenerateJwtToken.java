package com.mobilous.ext.plugin.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;

import java.util.Date;
import java.util.TimeZone;

public class GenerateJwtToken {

	public static String generatejwt() throws ParseException, UnsupportedEncodingException {

		Instant nbf = Instant.now().truncatedTo(ChronoUnit.SECONDS);
		Instant expiration = nbf.plus(5, ChronoUnit.MINUTES);

		String jws = Jwts.builder().setIssuer("AgricoProgram").setAudience("TSL-APIGEE-GenerateJWT-Agrico")
				.claim("appName", "AGRICO").claim("apikey", "EOkvQ3wbjcQmSnQj3yp4CmVjDVC5sU32")
				.setExpiration(Date.from(expiration)).setNotBefore(Date.from(nbf))
				.signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode("e94729ec-cba7-4ed4-a35f-4f0c80e3cf81"))
				.compact();
		System.out.println("JWT Token ================" + jws);

		return jws;

		
	}
}
