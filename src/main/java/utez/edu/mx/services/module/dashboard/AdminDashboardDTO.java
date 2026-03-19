package utez.edu.mx.services.module.dashboard;

import java.math.BigDecimal;

public class AdminDashboardDTO {

    private long totalUsuarios;
    private long totalEquipos;
    private long totalProyectos;
    private long proyectosActivos;
    private long proyectosCancelados;

    private long totalTareas;
    private long tareasCompletadas;
    private long tareasPendientes;

    private long totalPagos;
    private long pagosPagados;
    private long pagosPendientes;

    private BigDecimal montoTotalPagado;
    private BigDecimal montoTotalPendiente;

    public AdminDashboardDTO() {
    }

    public AdminDashboardDTO(
            long totalUsuarios,
            long totalEquipos,
            long totalProyectos,
            long proyectosActivos,
            long proyectosCancelados,
            long totalTareas,
            long tareasCompletadas,
            long tareasPendientes,
            long totalPagos,
            long pagosPagados,
            long pagosPendientes,
            BigDecimal montoTotalPagado,
            BigDecimal montoTotalPendiente
    ) {
        this.totalUsuarios = totalUsuarios;
        this.totalEquipos = totalEquipos;
        this.totalProyectos = totalProyectos;
        this.proyectosActivos = proyectosActivos;
        this.proyectosCancelados = proyectosCancelados;
        this.totalTareas = totalTareas;
        this.tareasCompletadas = tareasCompletadas;
        this.tareasPendientes = tareasPendientes;
        this.totalPagos = totalPagos;
        this.pagosPagados = pagosPagados;
        this.pagosPendientes = pagosPendientes;
        this.montoTotalPagado = montoTotalPagado;
        this.montoTotalPendiente = montoTotalPendiente;
    }

    public long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public long getTotalEquipos() {
        return totalEquipos;
    }

    public void setTotalEquipos(long totalEquipos) {
        this.totalEquipos = totalEquipos;
    }

    public long getTotalProyectos() {
        return totalProyectos;
    }

    public void setTotalProyectos(long totalProyectos) {
        this.totalProyectos = totalProyectos;
    }

    public long getProyectosActivos() {
        return proyectosActivos;
    }

    public void setProyectosActivos(long proyectosActivos) {
        this.proyectosActivos = proyectosActivos;
    }

    public long getProyectosCancelados() {
        return proyectosCancelados;
    }

    public void setProyectosCancelados(long proyectosCancelados) {
        this.proyectosCancelados = proyectosCancelados;
    }

    public long getTotalTareas() {
        return totalTareas;
    }

    public void setTotalTareas(long totalTareas) {
        this.totalTareas = totalTareas;
    }

    public long getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(long tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
    }

    public long getTareasPendientes() {
        return tareasPendientes;
    }

    public void setTareasPendientes(long tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public long getTotalPagos() {
        return totalPagos;
    }

    public void setTotalPagos(long totalPagos) {
        this.totalPagos = totalPagos;
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

    public BigDecimal getMontoTotalPagado() {
        return montoTotalPagado;
    }

    public void setMontoTotalPagado(BigDecimal montoTotalPagado) {
        this.montoTotalPagado = montoTotalPagado;
    }

    public BigDecimal getMontoTotalPendiente() {
        return montoTotalPendiente;
    }

    public void setMontoTotalPendiente(BigDecimal montoTotalPendiente) {
        this.montoTotalPendiente = montoTotalPendiente;
    }
}