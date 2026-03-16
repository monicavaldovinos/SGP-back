package utez.edu.mx.services.module.dashboard;

import java.util.List;

public class DashboardResponseDTO {

    private String nombre;
    private String apellidoPaterno;
    private int totalTareas;
    private int tareasCompletadas;
    private int tareasPendientes;
    private List<DashboardProyectoDTO> proyectos;

    public DashboardResponseDTO() {
    }

    public DashboardResponseDTO(
            String nombre,
            String apellidoPaterno,
            int totalTareas,
            int tareasCompletadas,
            int tareasPendientes,
            List<DashboardProyectoDTO> proyectos
    ) {
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.totalTareas = totalTareas;
        this.tareasCompletadas = tareasCompletadas;
        this.tareasPendientes = tareasPendientes;
        this.proyectos = proyectos;
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

    public List<DashboardProyectoDTO> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<DashboardProyectoDTO> proyectos) {
        this.proyectos = proyectos;
    }
}