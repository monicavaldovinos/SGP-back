package utez.edu.mx.services.module.pago.dto;

public class GenerarPagosPeriodoDTO {

    private Long idProyecto;
    private String periodo;

    public GenerarPagosPeriodoDTO() {
    }

    public GenerarPagosPeriodoDTO(Long idProyecto, String periodo) {
        this.idProyecto = idProyecto;
        this.periodo = periodo;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
}