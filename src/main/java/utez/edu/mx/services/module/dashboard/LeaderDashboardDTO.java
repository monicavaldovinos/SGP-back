package utez.edu.mx.services.module.dashboard;

import java.math.BigDecimal;

public class LeaderDashboardDTO {

    private String nombre;
    private String apellidoPaterno;

    private Long idEquipo;
    private String nombreEquipo;
    private int totalIntegrantes;

    private Long idProyecto;
    private String nombreProyecto;
    private String estadoProyecto;

    private int totalTareas;
    private int tareasCompletadas;
    private int tareasPendientes;

    private long pagosPagados;
    private long pagosPendientes;

    private BigDecimal montoPagado;
    private BigDecimal montoPendiente;

    public LeaderDashboardDTO() {
    }

    public LeaderDashboardDTO(
            String nombre,
            String apellidoPaterno,
            Long idEquipo,
            String nombreEquipo,
            int totalIntegrantes,
            Long idProyecto,
            String nombreProyecto,
            String estadoProyecto,
            int totalTareas,
            int tareasCompletadas,
            int tareasPendientes,
            long pagosPagados,
            long pagosPendientes,
            BigDecimal montoPagado,
            BigDecimal montoPendiente
    ) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.idEquipo = idEquipo;
        this.nombreEquipo = nombreEquipo;
        this.totalIntegrantes = totalIntegrantes;
        this.idProyecto = idProyecto;
        this.nombreProyecto = nombreProyecto;
        this.estadoProyecto = estadoProyecto;
        this.totalTareas = totalTareas;
        this.tareasCompletadas = tareasCompletadas;
        this.tareasPendientes = tareasPendientes;
        this.pagosPagados = pagosPagados;
        this.pagosPendientes = pagosPendientes;
        this.montoPagado = montoPagado;
        this.montoPendiente = montoPendiente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public Long getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Long idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public int getTotalIntegrantes() {
        return totalIntegrantes;
    }

    public void setTotalIntegrantes(int totalIntegrantes) {
        this.totalIntegrantes = totalIntegrantes;
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

    public int getTotalTareas() {
        return totalTareas;
    }

    public void setTotalTareas(int totalTareas) {
        this.totalTareas = totalTareas;
    }

    public int getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(int tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
    }

    public int getTareasPendientes() {
        return tareasPendientes;
    }

    public void setTareasPendientes(int tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public long getPagosPagados() {
        return pagosPagados;
    }

    public void setPagosPagados(long pagosPagados) {
        this.pagosPagados = pagosPagados;
    }

    public long getPagosPendientes() {
        return pagosPendientes;
    }

    public void setPagosPendientes(long pagosPendientes) {
        this.pagosPendientes = pagosPendientes;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public BigDecimal getMontoPendiente() {
        return montoPendiente;
    }

    public void setMontoPendiente(BigDecimal montoPendiente) {
        this.montoPendiente = montoPendiente;
    }
}