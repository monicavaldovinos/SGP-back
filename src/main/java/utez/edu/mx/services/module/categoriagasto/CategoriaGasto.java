package utez.edu.mx.services.module.categoriagasto;

import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORIA_GASTO")
public class CategoriaGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_seq")
    @SequenceGenerator(name = "categoria_seq", sequenceName = "SEQ_CATEGORIA_GASTO", allocationSize = 1)
    @Column(name = "id_categoria", nullable = false)
    private Long idCategoria;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    public CategoriaGasto() {}

    public CategoriaGasto(Long idCategoria, String nombre, String descripcion) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}