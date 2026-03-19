package utez.edu.mx.services.module.presupuesto;

import jakarta.persistence.*;
import utez.edu.mx.services.module.proyecto.Proyecto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PRESUPUESTO")
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "presupuesto_seq")
    @SequenceGenerator(name = "presupuesto_seq", sequenceName = "SEQ_PRESUPUESTO", allocationSize = 1)
    @Column(name = "id_presupuesto", nullable = false)
    private Long idPresupuesto;

    @OneToOne
    @JoinColumn(name = "id_proyecto", nullable = false, unique = true)
    private Proyecto proyecto;

    @Column(name = "monto_asignado", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoAsignado;

    @Column(name = "monto_utilizado", precision = 10, scale = 2)
    private BigDecimal montoUtilizado;

    @Column(name = "monto_disponible", precision = 10, scale = 2)
    private BigDecimal montoDisponible;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    public Presupuesto() {}

    public Presupuesto(Long idPresupuesto, Proyecto proyecto, BigDecimal montoAsignado,
                       BigDecimal montoUtilizado, BigDecimal montoDisponible, LocalDate fechaRegistro) {
        this.idPresupuesto = idPresupuesto;
        this.proyecto = proyecto;
        this.montoAsignado = montoAsignado;
        this.montoUtilizado = montoUtilizado;
        this.montoDisponible = montoDisponible;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getIdPresupuesto() { return idPresupuesto; }
    public void setIdPresupuesto(Long idPresupuesto) { this.idPresupuesto = idPresupuesto; }

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public BigDecimal getMontoAsignado() { return montoAsignado; }
    public void setMontoAsignado(BigDecimal montoAsignado) { this.montoAsignado = montoAsignado; }

    public BigDecimal getMontoUtilizado() { return montoUtilizado; }
    public void setMontoUtilizado(BigDecimal montoUtilizado) { this.montoUtilizado = montoUtilizado; }

    public BigDecimal getMontoDisponible() { return montoDisponible; }
    public void setMontoDisponible(BigDecimal montoDisponible) { this.montoDisponible = montoDisponible; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}