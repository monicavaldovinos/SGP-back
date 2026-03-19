package utez.edu.mx.services.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoRecuperacion(String destino, String nombreUsuario, String token) {
        String enlace = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Recuperación de contraseña - SGP");
        mensaje.setText(
                "Hola " + nombreUsuario + ",\n\n" +
                        "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                        "Da clic en el siguiente enlace para continuar:\n" +
                        enlace + "\n\n" +
                        "Este enlace expirará en 30 minutos.\n\n" +
                        "Si tú no solicitaste este cambio, puedes ignorar este correo."
        );

        mailSender.send(mensaje);
    }
}