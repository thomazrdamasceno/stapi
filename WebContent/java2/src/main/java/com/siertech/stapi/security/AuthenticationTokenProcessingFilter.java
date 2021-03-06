package com.siertech.stapi.security;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.siertech.stapi.usersystem.UserLoginCache;



@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean
{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
	ServletException
	{


		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		String userName = TokenUtils.getUserNameFromToken(authToken);


		if (userName != null && !userName.equals("undefined") && UserLoginCache.get(userName)!=null) {

			AccountUserDetails userDetails = new AccountUserDetails(UserLoginCache.get(userName));

			userDetails.getAccount().setCurrentFilialId(extractCurrentFilialIdFromRequest(httpRequest));
			
			userDetails.getAccount().setCurrentOperadorId(extractCurrentOperadorIdFromRequest(httpRequest));

			if (TokenUtils.validateToken(authToken, userDetails)) {

				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

				SecurityContextHolder.getContext().setAuthentication(authentication);

			}
		}
		else{

			SecurityContextHolder.getContext().setAuthentication(null);
		}

		chain.doFilter(request, response);
	}


	private HttpServletRequest getAsHttpRequest(ServletRequest request)
	{
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}


	private Long extractCurrentFilialIdFromRequest(HttpServletRequest httpRequest)
	{

		return Long.parseLong(httpRequest.getParameter("filialId"));

	}
	
	private Long extractCurrentOperadorIdFromRequest(HttpServletRequest httpRequest)
	{

		return Long.parseLong(httpRequest.getParameter("operadorId"));

	}



	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest)
	{
		/* Get token from header */
		String authToken = httpRequest.getHeader("X-Auth-Token");

		/* If token not found get it from request parameter */
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}

		return authToken;
	}
}