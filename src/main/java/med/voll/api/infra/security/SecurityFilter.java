package med.voll.api.infra.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// O @Component é utilizado para que o Spring carregue uma classe/componente genérico
// Para realizar um filtro seguindo o servlet é necessário implementar a interface Filter do jakarta.servlet.Filter
// Contudo, nesse projeto será utiliza uma classe do próprio spring, que ira facilitar todo o processo
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if(tokenJWT != null){
            var subject = tokenService.getSubject(tokenJWT);
            var user =  repository.findByLogin(subject);

            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Necessário para chamar os próximos filtros da aplicação
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {

        // O envio de um token é realizado em um cabeçalho do Protocolo HTTP
        // Cabeçalho "Authorization"
        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null){
            // Por padrão, o tipo de prefixo "Bearer" é utilizado pata tokens JWT
            // Como precisamos apenas da string que representa o tokenJWT, retiramos o Bearer
            return authorizationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
