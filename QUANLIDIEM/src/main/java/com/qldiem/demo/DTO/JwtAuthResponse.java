package com.qldiem.demo.DTO;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@Data
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String userId;
    private Collection<? extends GrantedAuthority> roles;

    public JwtAuthResponse(String accessToken, String username, String userId, Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }
}
