package utez.edu.mx.services.module.pago;

import jakarta.persistence.*;
import utez.edu.mx.services.module.categoriagasto.CategoriaGasto;
import utez.edu.mx.services.module.proyecto.Proyecto;
import utez.edu.mx.services.module.usuario.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "PAGO")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pago_seq")
    @SequenceGenerator(name = "pago_seq", sequenceName = "SEQ_PAGO", allocationSize = 1)
    @Column(name = "id_pago", nullable = false)
    private Long idPago;

    @Column(name = "concepto", nullable = false, length = 150)
    private String concepto;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "horas", precision = 5, scale = 2)
    private BigDecimal horas;

    @Column(name = "tarifa", precision = 10, scale = 2)
    private BigDecimal tarifa;

    @Column(name = "forma_pago", nullable = false, length = 50)
    private String formaPago;

    @Column(name = "periodo", length = 50)
    private String periodo;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "estatus", nullable = false, length = 20)
    private String estatus; // PENDIENTE, RECIBIDO

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaGasto categoria;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_registrado_por", nullable = false)
    private Usuario registradoPor;

    public Pago() {}

    public Long getIdPago() { return idPago; }
    public void setIdPago(Long idPago) { this.idPago = idPago; }

    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public BigDecimal getHoras() { return horas; }
    public void setHoras(BigDecimal horas) { this.horas = horas; }

    public BigDecimal getTarifa() { return tarifa; }
    public void setTarifa(BigDecimal tarifa) { this.tarifa = tarifa; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getEstatus() { return estatus; }
    public void setEstatus(String estatus) { this.estatus = estatus; }

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public CategoriaGasto getCategoria() { return categoria; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Usuario getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(Usuario registradoPor) { this.registradoPor = registradoPor; }
}