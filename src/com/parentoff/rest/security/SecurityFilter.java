package com.parentoff.rest.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parentoff.rest.config.ConfigHelper;

import sun.misc.BASE64Decoder;

public class SecurityFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		String username = ConfigHelper.getSpecificValue("username");
		String password = ConfigHelper.getSpecificValue("password");
		String decoded;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		try {
			String header = ((HttpServletRequest) request)
					.getHeader("authorization");
			if (header != null) {
				String data = header.substring(header.indexOf(" ") + 1);

				byte[] bytes = new BASE64Decoder().decodeBuffer(data);
				decoded = new String(bytes);

				if ((username + ":" + password).equals(decoded)) {
					filter.doFilter(request, response);
				} else {
					httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} else {
				httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (Exception e) {
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
