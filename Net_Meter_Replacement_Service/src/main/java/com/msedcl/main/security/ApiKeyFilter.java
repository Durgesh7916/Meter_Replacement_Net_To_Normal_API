package com.msedcl.main.security;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    // HARD CODED API KEY
   // private static final String API_KEY =
          //  "MAITRI_SOLAR_API";

    
    @Value("${api.key}")
    private String API_KEY;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String apiKey =
                request.getHeader("x-api-key");

        // Skip Swagger
        String path = request.getRequestURI();
        if (path.contains("/Admin")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (path.contains("swagger") ||
                path.contains("api-docs")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Validate API Key
        if (apiKey == null ||
                !apiKey.equals(API_KEY)) {

            response.setStatus(
                    HttpStatus.UNAUTHORIZED.value());

            response.setContentType("application/json");

            ObjectMapper mapper = new ObjectMapper();

            response.getWriter().write(
                    mapper.writeValueAsString(
                            new ErrorResponse(
                                    "FAILED",
                                    "Invalid API Key"
                            )
                    )
            );

            return;
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}