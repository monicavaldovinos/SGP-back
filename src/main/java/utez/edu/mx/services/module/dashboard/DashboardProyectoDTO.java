package utez.edu.mx.services.module.dashboard;

public class DashboardProyectoDTO {

    private Long idProyecto;
    private String nombreProyecto;
    private String estadoProyecto;
    private double porcentajeAvance;

    public DashboardProyectoDTO() {
    }

    public DashboardProyectoDTO(Long idProyecto, String nombreProyecto, String estadoProyecto, double porcentajeAvance) {
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
        this.estadoProyecto = estadoProyecto;
        this.porcentajeAvance = porcentajeAvance;
    }

    public Long getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Long idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getEstadoProyecto() {
        return estadoProyecto;
    }

    public void setEstadoProyecto(String estadoProyecto) {
        this.estadoProyecto = estadoProyecto;
    }

    public double getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(double porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }
}