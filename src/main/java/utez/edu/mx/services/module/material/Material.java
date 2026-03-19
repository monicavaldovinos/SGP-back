package utez.edu.mx.services.module.material;

import jakarta.persistence.*;
import utez.edu.mx.services.module.categoriagasto.CategoriaGasto;
import utez.edu.mx.services.module.proyecto.Proyecto;

import java.math.BigDecimal;

@Entity
@Table(name = "MATERIAL")
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "material_seq")
    @SequenceGenerator(name = "material_seq", sequenceName = "SEQ_MATERIAL", allocationSize = 1)
    @Column(name = "id_material", nullable = false)
    private Long idMaterial;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private CategoriaGasto categoria;

    public Material() {}

    public Material(Long idMaterial, String nombre, Integer cantidad, BigDecimal precio,
                    BigDecimal total, Proyecto proyecto, CategoriaGasto categoria) {
        this.idMaterial = idMaterial;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.proyecto = proyecto;
        this.categoria = categoria;
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

    public Proyecto getProyecto() { return proyecto; }
    public void setProyecto(Proyecto proyecto) { this.proyecto = proyecto; }

    public CategoriaGasto getCategoria() { return categoria; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
}