package org.zerock.allivery.config;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider { //JWT 토큰을 생성 및 검증 모듈
    //https://developerbee.tistory.com/201?category=474042
    @Value("spring.jwt.secret") 
    private String secretKey;

    private long tokenValidMillisecond = 1000L * 60 * 60; //1시간 토큰 유효

    //UserDetailsService는 스프링 시큐리티에서 유저의 정보를 가져옴, 유저의 정보를 불러와서 UserDetails로 리턴해준다.
    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String userEmail, List<String> roles){
        Claims claims = Jwts.claims().setSubject(userEmail); //데이터 = Claims
        claims.put("roles", roles);
        Date now =  new Date();
        return Jwts.builder()
                .setClaims(claims)//데이터
                .setIssuedAt(now)//토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond)) //토큰 유효시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey) //암호화 알고리즘
                .compact();
    }

    //Jwt토큰으로 인증정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //Jwt 토큰에서 회원 구별 정보 추출
    public String getUserPk(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //Request의 Header에서 token파싱 : "X-AUTH-TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest req){
        return req.getHeader("X-AUTH-TOKEN");
    }

    // Jwt토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }
}
