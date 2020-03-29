package com.himorfosis.moneymanagement.security.jwt;

import com.himorfosis.moneymanagement.utilities.Util;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtSecurityDetailService jwtSecurityDetailService;

    private final JwtSecurityToken jwtSecurityToken;
    private String Authorization = "Authorization";
    private String Bearer = " Bearer";
    private String TAG = "JwtRequestFilter";

    public JwtRequestFilter(JwtSecurityToken jwtSecurityToken) {
        this.jwtSecurityToken = jwtSecurityToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        isError("Filter Start ...");

        String requestTokenHeader = httpServletRequest.getHeader("Authorization");
        String email = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                 email = jwtSecurityToken.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                isError("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                isError("JWT Token has expired");
            }
        } else {
            isError("JWT Token does not begin with Bearer String");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            isError("Email : " + email);

            UserDetails userDetails = this.jwtSecurityDetailService.loadUserByUsername(email);
            if (jwtSecurityToken.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    private void isError(String message) {
        Util.log(TAG, message);
    }
}
