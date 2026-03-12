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

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder,
                          UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<AppiResponse> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AppiResponse("Credenciales incorrectas", HttpStatus.UNAUTHORIZED));
        }

        Usuario usuario = (Usuario) userDetailsService.loadUserByUsername(username);
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
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El username ya está en uso", HttpStatus.BAD_REQUEST));
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.badRequest()
                    .body(new AppiResponse("El correo ya está en uso", HttpStatus.BAD_REQUEST));
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setEstatus("ACTIVO");
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.ok(new AppiResponse("Usuario registrado exitosamente", saved, HttpStatus.OK));
    }
}