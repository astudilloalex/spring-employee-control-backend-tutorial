package com.alexastudillo.employee.security.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.alexastudillo.employee.handlers.ResponseHandler;
import com.alexastudillo.employee.security.utilities.JWTTokenUtil;
import com.alexastudillo.employee.security.utilities.SecurityConstanst;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private final JWTTokenUtil jwtTokenUtil;

	public JWTAuthorizationFilter(final AuthenticationManager authenticationManager, final JWTTokenUtil jwtTokenUtil) {
		super(authenticationManager);
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain) throws IOException, ServletException {
		final String header = request.getHeader(SecurityConstanst.HEADER_STRING);
		if (header == null || !header.startsWith(SecurityConstanst.TOKEN_PREFIX)) {
			responseClient(response, new ResponseHandler().generateResponseWithoutData("security-token-required",
					HttpStatus.UNAUTHORIZED));
			return;
		}
		final String token = request.getHeader(SecurityConstanst.HEADER_STRING).replace(SecurityConstanst.TOKEN_PREFIX,
				"");
		if (!jwtTokenUtil.validate(token)) {
			String message = "invalid-token";
			try {
				jwtTokenUtil.getExpirationDate(token);
			} catch (TokenExpiredException e) {
				message = "token-has-expired";
			}
			responseClient(response,
					new ResponseHandler().generateResponseWithoutData(message, HttpStatus.UNAUTHORIZED));
			return;
		}
		final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				jwtTokenUtil.getUsername(token), null, jwtTokenUtil.getAuthorities(token));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private void responseClient(final HttpServletResponse response, final ResponseEntity<Object> responseEntity) {
		try {
			response.setStatus(responseEntity.getStatusCodeValue());
			response.setContentType("application/json");
			response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity.getBody()));
			response.getWriter().flush();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

}
