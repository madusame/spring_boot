package com.madusame.springboot.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.madusame.springboot.modelo.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {

	@Value("${br.com.madusame.expirationMs}")
	private String expirationMs;
	
	@Value("${br.com.madusame.secret}")
	private String secret;
	
	public String generateToken(Authentication authenticate) {
		Usuario cliente = (Usuario) authenticate.getPrincipal();
		Date hoje = new Date();
 		Date expiration = new Date(hoje.getTime() + Long.parseLong(this.expirationMs));
	
		return Jwts.builder()
				.setIssuer("API Forum da Alura(Teste)")
				.setSubject(cliente.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, this.secret)
				.compact();
	}

	// Verifica o token passado pelo filter
	public Boolean verifyToken(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	// Pega o Usuario para o filter fazer a authenticação
	public Long getUserId(String token) {
		String id = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		return Long.parseLong(id);
	}
	
	
}
