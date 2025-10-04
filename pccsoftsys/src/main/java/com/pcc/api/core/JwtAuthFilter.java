package com.pcc.api.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthFilter implements Filter {

	static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

	private static final String AUTH_HEADER_KEY = "Authorization";
	protected FilterConfig filterConfig;

	// with trailing space to separate token
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer ";

	private static final int STATUS_CODE_UNAUTHORIZED = 401;

	public JwtAuthFilter() {
		logger.info("JwtAuthFilter -- constructor");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		logger.info("JwtAuthenticationFilter initialized");
	}

	@Override
	public void doFilter(final ServletRequest servletRequest,
			final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

		//System.out.println("IN  ContentCachingFilter "); //ต้องให้ตัวนี้ทำงานเป็น filter ตัวแรก
		CachedBodyHttpServletRequest request = new CachedBodyHttpServletRequest(httpRequest);

		try {

			String jwt = getBearerToken(request);

			if (jwt != null && !jwt.isEmpty()) {

				JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
				jwtTokenUtil.getUsernameFromToken(jwt);// จะ error ทันทีถ้า expire
				//String username = jwtTokenUtil.getUsernameFromToken(jwt);// จะ error ทันทีถ้า expire
				//LOG.info("username =" + username);
				//LOG.info("Logged in using JWT");
			} else {
				logger.info("No JWT provided, go on unauthenticated");
				throw new IOException("unauthenticated");
			}

			filterChain.doFilter(request, servletResponse);

			//System.out.println("===> end JwtAuthFilter"); //เมื่อ response ค่าแล้วตัวนี้จะทำงานสุดท้าย

		} catch (final Exception e) {
			logger.info("Failed logging in with security token", e);
			HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
			httpResponse.setContentLength(0);
			httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
		}

	}

	@Override
	public void destroy() {
		logger.info("JwtAuthenticationFilter destroyed");
	}

	/**
	 * Get the bearer token from the HTTP request. The token is in the HTTP request
	 * "Authorization" header in the form of: "Bearer [token]"
	 */
	private String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTH_HEADER_KEY);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}

}