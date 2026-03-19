package utez.edu.mx.services.module.material.dto;

import utez.edu.mx.services.module.categoriagasto.CategoriaGasto;
import utez.edu.mx.services.module.material.Material;
import utez.edu.mx.services.module.proyecto.ProyectoDTO;

import java.math.BigDecimal;

public class MaterialDTO {

    private Long idMaterial;
    private String nombre;
    private Integer cantidad;
    private BigDecimal precio;
    private BigDecimal total;
    private ProyectoDTO proyecto;
    private CategoriaGasto categoria;

    public MaterialDTO() {}

    public MaterialDTO(Material material) {
        this.idMaterial = material.getIdMaterial();
        this.nombre = material.getNombre();
        this.cantidad = material.getCantidad();
        this.precio = material.getPrecio();
        this.total = material.getTotal();
        this.proyecto = new ProyectoDTO(material.getProyecto());
        this.categoria = material.getCategoria();
    }

    public Long getIdMaterial() { return idMaterial; }
    public void setIdMaterial(Long idMaterial) { this.idMaterial = idMaterial; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public ProyectoDTO getProyecto() { return proyecto; }
    public void setProyecto(ProyectoDTO proyecto) { this.proyecto = proyecto; }

    public CategoriaGasto getCategoria() { return categoria; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
}