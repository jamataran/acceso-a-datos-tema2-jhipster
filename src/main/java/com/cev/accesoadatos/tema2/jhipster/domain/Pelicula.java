package com.cev.accesoadatos.tema2.jhipster.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pelicula.
 */
@Entity
@Table(name = "pelicula")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pelicula")
public class Pelicula implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "titulo", length = 255, nullable = false)
    private String titulo;

    @Column(name = "fecha_estreno")
    private Instant fechaEstreno;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "en_cines")
    private Boolean enCines;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pelicula id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public Pelicula titulo(String titulo) {
        this.setTitulo(titulo);
        return this;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Instant getFechaEstreno() {
        return this.fechaEstreno;
    }

    public Pelicula fechaEstreno(Instant fechaEstreno) {
        this.setFechaEstreno(fechaEstreno);
        return this;
    }

    public void setFechaEstreno(Instant fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Pelicula descripcion(String descripcion) {
        this.setDescripcion(descripcion);
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEnCines() {
        return this.enCines;
    }

    public Pelicula enCines(Boolean enCines) {
        this.setEnCines(enCines);
        return this;
    }

    public void setEnCines(Boolean enCines) {
        this.enCines = enCines;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pelicula)) {
            return false;
        }
        return id != null && id.equals(((Pelicula) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pelicula{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", fechaEstreno='" + getFechaEstreno() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", enCines='" + getEnCines() + "'" +
            "}";
    }
}
