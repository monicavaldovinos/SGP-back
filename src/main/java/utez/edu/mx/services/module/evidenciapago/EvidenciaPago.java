package utez.edu.mx.services.module.evidenciapago;

import jakarta.persistence.*;
import utez.edu.mx.services.module.pago.Pago;

import java.time.LocalDate;

@Entity
@Table(name = "EVIDENCIA_PAGO")
public class EvidenciaPago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evidencia_seq")
    @SequenceGenerator(name = "evidencia_seq", sequenceName = "SEQ_EVIDENCIA_PAGO", allocationSize = 1)
    @Column(name = "id_evidencia", nullable = false)
    private Long idEvidencia;

    @ManyToOne
    @JoinColumn(name = "id_pago", nullable = false)
    private Pago pago;

    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;

    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;

    @Column(name = "tipo_archivo", length = 50)
    private String tipoArchivo;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDate fechaSubida;

    public EvidenciaPago() {}

    public Long getIdEvidencia() { return idEvidencia; }
    public void setIdEvidencia(Long idEvidencia) { this.idEvidencia = idEvidencia; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public String getNombreArchivo() { return nombreArchivo; }
    public void setNombreArchivo(String nombreArchivo) { this.nombreArchivo = nombreArchivo; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public String getTipoArchivo() { return tipoArchivo; }
    public void setTipoArchivo(String tipoArchivo) { this.tipoArchivo = tipoArchivo; }

    public LocalDate getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDate fechaSubida) { this.fechaSubida = fechaSubida; }
}