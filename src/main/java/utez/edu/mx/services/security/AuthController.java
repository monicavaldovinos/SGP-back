package utez.edu.mx.services.security;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.services.kernel.AppiResponse;
import utez.edu.mx.services.module.usuario.Usuario;
import utez.edu.mx.services.module.usuario.UsuarioService;
import utez.edu.mx.services.module.usuario.UsuarioRepository;
import utez.edu.mx.services.module.usuario.dto.UsuarioDTO;
import utez.edu.mx.services.security.dto.ForgotPasswordRequestDTO;
import utez.edu.mx.services.security.dto.ResetPasswordRequestDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/sgp-api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserDetailsServiceImpl userDetailsService,
            UsuarioService usuarioService,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @PostMapping("/login")
    public ResponseEntity<AppiResponse> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Username y password son obligatorios", HttpStatus.BAD_REQUEST));
        }

        if (usuarioService.estaBloqueado(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AppiResponse(
                            "Has excedido el número máximo de intentos. Tu cuenta ha sido bloqueada temporalmente por 30 minutos.",
                            HttpStatus.FORBIDDEN
                    ));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            usuarioService.registrarIntentoFallido(username);

            if (usuarioService.estaBloqueado(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new AppiResponse(
                                "Has excedido el número máximo de intentos. Tu cuenta ha sido bloqueada temporalmente por 30 minutos.",
                                HttpStatus.FORBIDDEN
                        ));
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppiResponse("Credenciales incorrectas", HttpStatus.UNAUTHORIZED));
        }

        usuarioService.resetearIntentos(username);

        Usuario usuario = usuarioService.findEntityByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después del login"));

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AppiResponse("Usuario desactivado", HttpStatus.FORBIDDEN));
        }

        String token = jwtUtil.generateToken(usuario);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("usuario", new UsuarioDTO(usuario));

        return ResponseEntity.ok(
                new AppiResponse("Login exitoso", data, HttpStatus.OK)
        );
    }

    @PostMapping("/registro")
    public ResponseEntity<AppiResponse> registro(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @GetMapping("/me")
    public ResponseEntity<AppiResponse> me(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader
    ) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppiResponse("Token no proporcionado", HttpStatus.UNAUTHORIZED));
        }

        String token = authorizationHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);

            return usuarioService.findEntityByUsername(username)
                    .map(usuario -> ResponseEntity.ok(
                            new AppiResponse("Usuario autenticado", new UsuarioDTO(usuario), HttpStatus.OK)
                    ))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new AppiResponse("Usuario no encontrado", HttpStatus.NOT_FOUND)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppiResponse("Token inválido o expirado", HttpStatus.UNAUTHORIZED));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<AppiResponse> forgotPassword(@RequestBody ForgotPasswordRequestDTO dto) {
        try {
            System.out.println("ENTRE A forgot-password");
            System.out.println("Correo recibido: " + dto.getCorreo());

            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(dto.getCorreo());

            if (usuarioOpt.isEmpty()) {
                System.out.println("No existe usuario con ese correo");
                return ResponseEntity.badRequest()
                        .body(new AppiResponse("No existe un usuario con ese correo", HttpStatus.BAD_REQUEST));
            }

            Usuario usuario = usuarioOpt.get();
            System.out.println("Usuario encontrado: " + usuario.getUsername());

            String token = UUID.randomUUID().toString();
            usuario.setTokenRecuperacion(token);
            usuario.setExpiracionToken(LocalDateTime.now().plusMinutes(30));

            usuarioRepository.save(usuario);
            System.out.println("Token guardado correctamente");

            mailService.enviarCorreoRecuperacion(
                    usuario.getCorreo(),
                    usuario.getNombre(),
                    token
            );
            System.out.println("Correo enviado correctamente");

            return ResponseEntity.ok(
                    new AppiResponse("Se envió el enlace de recuperación a tu correo", HttpStatus.OK)
            );

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AppiResponse(
                            "Error al procesar la recuperación: " + e.getMessage(),
                            HttpStatus.INTERNAL_SERVER_ERROR
                    ));
        }
    }

    @GetMapping("/validate-reset-token")
    public ResponseEntity<AppiResponse> validateResetToken(@RequestParam String token) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenRecuperacion(token);

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Token de recuperación inválido", HttpStatus.BAD_REQUEST));
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getExpiracionToken() == null || LocalDateTime.now().isAfter(usuario.getExpiracionToken())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El token de recuperación ha expirado", HttpStatus.BAD_REQUEST));
        }

        Map<String, Object> data = new HashMap<>();
        data.put("valido", true);
        data.put("correo", usuario.getCorreo());
        data.put("username", usuario.getUsername());

        return ResponseEntity.ok(
                new AppiResponse("Token válido", data, HttpStatus.OK)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<AppiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenRecuperacion(dto.getToken());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("Token de recuperación inválido", HttpStatus.BAD_REQUEST));
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getExpiracionToken() == null || LocalDateTime.now().isAfter(usuario.getExpiracionToken())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El token de recuperación ha expirado", HttpStatus.BAD_REQUEST));
        }

        if (!usuarioService.passwordSeguraPublic(dto.getNewPassword())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse(
                            "La contraseña no cumple con los requisitos: mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial (@#$%&*!)",
                            HttpStatus.BAD_REQUEST
                    ));
        }

        usuario.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        usuario.setTokenRecuperacion(null);
        usuario.setExpiracionToken(null);
        usuario.setIntentosFallidos(0);
        usuario.setFechaBloqueo(null);

        if ("BLOQUEADO".equalsIgnoreCase(usuario.getEstatus())) {
            usuario.setEstatus("ACTIVO");
        }

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                new AppiResponse("Contraseña restablecida correctamente", HttpStatus.OK)
        );
    }
}