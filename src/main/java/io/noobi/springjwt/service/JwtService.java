package io.noobi.springjwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.noobi.springjwt.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

  private final String SECRET_KEY = "56bb5fe32bf1833ede18818355128b25953ef4615beacc5e847c43723f41c830";

  public String extractUsername(String token){
    return extractClaim(token, Claims::getSubject);
  }

  public boolean isValid(String token, UserDetails userDetails){
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token){
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token){
    return Jwts
        .parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String generateToken(User user){
    String token = Jwts
        .builder()
        .subject(user.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(getSignInKey())
        .compact();

    return token;
  }

  private SecretKey getSignInKey(){
    byte keyBytes[] = Decoders.BASE64URL.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
