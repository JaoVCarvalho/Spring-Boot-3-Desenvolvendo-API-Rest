package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {

        try{
            // É uma má prática de programação deixar evidente no código algum dado sensível, como exemplo, a senha utilizada no algoritmo HMAC256
            // Para isso, vamos configurar nas propriedades da aplicação
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API Voll.med")
                    .withSubject(usuario.getLogin())
                    //.withClaim(Chave, valor) -> Podemos adicionar diversas outras informações ao token
                    .withExpiresAt(dataExpericao()) // Para evitar falhas de segurança, um token precisa de uma data de validade
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar um token", exception);
        }
    }

    private Instant dataExpericao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
