package utez.edu.mx.services.module.pago;

import utez.edu.mx.services.module.categoriagasto.CategoriaGasto;
import utez.edu.mx.services.module.pago.Pago;
import utez.edu.mx.services.module.proyecto.ProyectoDTO;
import utez.edu.mx.services.module.usuario.dto.UsuarioDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoDTO {

    private Long idPago;
    private String concepto;
    private String descripcion;
    private BigDecimal monto;
    private BigDecimal horas;
    private BigDecimal tarifa;
    private String formaPago;
    private String periodo;
    private LocalDate fechaPago;
    private String estatus;
    private ProyectoDTO proyecto;
    private CategoriaGasto categoria;
    private UsuarioDTO usuario;
    private UsuarioDTO registradoPor;

    public PagoDTO() {}

    public PagoDTO(Pago pago) {
        this.idPago = pago.getIdPago();
        this.concepto = pago.getConcepto();
        this.descripcion = pago.getDescripcion();
        this.monto = pago.getMonto();
        this.horas = pago.getHoras();
        this.tarifa = pago.getTarifa();
        this.formaPago = pago.getFormaPago();
        this.periodo = pago.getPeriodo();
        this.fechaPago = pago.getFechaPago();
        this.estatus = pago.getEstatus();
        this.proyecto = new ProyectoDTO(pago.getProyecto());
        this.categoria = pago.getCategoria();
        this.usuario = new UsuarioDTO(pago.getUsuario());
        this.registradoPor = new UsuarioDTO(pago.getRegistradoPor());
    }

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

    public ProyectoDTO getProyecto() { return proyecto; }
    public void setProyecto(ProyectoDTO proyecto) { this.proyecto = proyecto; }

    public CategoriaGasto getCategoria() { return categoria; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }

    public UsuarioDTO getUsuario() { return usuario; }
    public void setUsuario(UsuarioDTO usuario) { this.usuario = usuario; }

    public UsuarioDTO getRegistradoPor() { return registradoPor; }
    public void setRegistradoPor(UsuarioDTO registradoPor) { this.registradoPor = registradoPor; }
}