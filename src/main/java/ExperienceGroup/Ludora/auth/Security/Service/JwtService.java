package ExperienceGroup.Ludora.auth.Security.Service;

import io.jsonwebtoken.Claims;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value ("${jwt.secret}")
    private String jwtSecretKey;

   @Value("{jwt.expiration}")
    private Long jwtExpiration;


   public String extractUsername(String token){
       return extractClaim(token, Claims::getSubject);
   }


}
