package com.adminmodule.security;

import com.adminmodule.domain.Account;
import com.adminmodule.exceptionResponse.userException.UserBadRequestException;
import com.adminmodule.service.CustomerService;
import com.adminmodule.service.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthService {
    private final CustomerService customerService;

    private final AuthenticationManager authenticationManager;

    private final AccountDetailsService accountDetailsService;

    private final JWTUtility jwtUtility;

    @Autowired
    public AuthService(CustomerService customerService,
                              AuthenticationManager authenticationManager,
                              AccountDetailsService accountDetailsService,
                              JWTUtility jwtUtility) {
        this.customerService = customerService;
        this.authenticationManager = authenticationManager;
        this.accountDetailsService = accountDetailsService;
        this.jwtUtility = jwtUtility;
    }

    public ResponseEntity<Object> generateToken(@RequestBody Account account){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(account.getEmail(), account.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = accountDetailsService.loadUserByUsername(account.getEmail());
            final String token = jwtUtility.generateToken(userDetails);
            Map<String, Object> response = new HashMap<>();
            CustomerDTO customerDTO = customerService.getCustomerByUsername(account.getEmail());
            customerDTO.getAccount().setPassword("********");
            response.put("bearerToken", token);
            response.put("user", customerDTO);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (AuthenticationException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
