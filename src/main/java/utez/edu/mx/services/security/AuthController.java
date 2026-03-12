package utez.edu.mx.services.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioRepository;
import utez.edu.mx.services.module.usuario.UsuarioService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sgp-api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                          UserDetailsServiceImpl userDetailsService, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<AppiResponse> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Verificar si la cuenta está bloqueada
        if (usuarioService.estaBloqueado(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AppiResponse(
                            "Has excedido el número máximo de intentos. Tu cuenta ha sido bloqueada temporalmente por 30 minutos.",
                            HttpStatus.FORBIDDEN));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            usuarioService.registrarIntentoFallido(username);

            // Verificar si se bloqueó en este intento
            if (usuarioService.estaBloqueado(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new AppiResponse(
                                "Has excedido el número máximo de intentos. Tu cuenta ha sido bloqueada temporalmente por 30 minutos.",
                                HttpStatus.FORBIDDEN));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppiResponse("Credenciales incorrectas", HttpStatus.UNAUTHORIZED));
        }

        // Login exitoso — resetear intentos
        usuarioService.resetearIntentos(username);

        Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(username);

        // Verificar que el usuario esté activo
        if (!"ACTIVO".equals(usuario.getEstatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AppiResponse("Usuario desactivado", HttpStatus.FORBIDDEN));
        }

        String token = jwtUtil.generateToken(usuario);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", usuario.getUsername());
        data.put("nombre", usuario.getNombre());
        data.put("rol", usuario.getRol().getNombre());
        data.put("idUsuario", usuario.getIdUsuario());

        return ResponseEntity.ok(new AppiResponse("Login exitoso", data, HttpStatus.OK));
    }

    @PostMapping("/registro")
    public ResponseEntity<AppiResponse> registro(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }
}