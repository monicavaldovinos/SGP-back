package utez.edu.mx.services.module.pago.dto;

import java.math.BigDecimal;

public class RealizarPagoDTO {

    private BigDecimal horas;
    private BigDecimal tarifa;
    private String descripcion;

    public RealizarPagoDTO() {
    }

    public RealizarPagoDTO(BigDecimal horas, BigDecimal tarifa, String descripcion) {
        this.horas = horas;
        this.tarifa = tarifa;
        this.descripcion = descripcion;
    }

    public BigDecimal getHoras() {
        return horas;
    }

    public void setHoras(BigDecimal horas) {
        this.horas = horas;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public void setTarifa(BigDecimal tarifa) {
        this.tarifa = tarifa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}