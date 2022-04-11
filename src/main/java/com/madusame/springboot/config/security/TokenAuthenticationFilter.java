package com.madusame.springboot.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.madusame.springboot.modelo.Usuario;
import com.madusame.springboot.repository.UsuarioRepository;

// Apesar desta classe ser do spring,a mesma não possui injeção
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	
	private UsuarioRepository usuarioRepository;
	
	//Fazendo a injeção de dependência padrão pois esta classe não suporta anotação spring
	public TokenAuthenticationFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = getToken(request);
		Boolean valid = this.tokenService.verifyToken(token);
		if(valid) {
			autenticarCliente(token);
		}
		// Seguir a Aplicação normalmente
		filterChain.doFilter(request, response);
	}

	// Autenticando de forma forçada(não passando o username/password com o AuthenticationService)
	private void autenticarCliente(String token) {
		Long userId = this.tokenService.getUserId(token);
		Usuario usuario = this.usuarioRepository.findById(userId).get();
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	// pegando o token através do header Authorization
	private String getToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || !token.startsWith("Bearer ") || token.isEmpty()) {
			return null;
		}
		
		return token.substring(7,token.length());
	}

}
