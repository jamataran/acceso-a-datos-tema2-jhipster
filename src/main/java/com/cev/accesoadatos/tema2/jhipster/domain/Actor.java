package com.cev.accesoadatos.tema2.jhipster.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "actor")
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "nombre", length = 255, nullable = false)
    private String nombre;

    @NotNull
    @Size(min = 10, max = 255)
    @Column(name = "apellidos", length = 255, nullable = false)
    private String apellidos;

    @ManyToMany(mappedBy = "actors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "estreno", "actors" }, allowSetters = true)
    private Set<Pelicula> peliculas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Actor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Actor nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public Actor apellidos(String apellidos) {
        this.setApellidos(apellidos);
        return this;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Set<Pelicula> getPeliculas() {
        return this.peliculas;
    }

    public void setPeliculas(Set<Pelicula> peliculas) {
        if (this.peliculas != null) {
            this.peliculas.forEach(i -> i.removeActor(this));
        }
        if (peliculas != null) {
            peliculas.forEach(i -> i.addActor(this));
        }
        this.peliculas = peliculas;
    }

    public Actor peliculas(Set<Pelicula> peliculas) {
        this.setPeliculas(peliculas);
        return this;
    }

    public Actor addPelicula(Pelicula pelicula) {
        this.peliculas.add(pelicula);
        pelicula.getActors().add(this);
        return this;
    }

    public Actor removePelicula(Pelicula pelicula) {
        this.peliculas.remove(pelicula);
        pelicula.getActors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actor)) {
            return false;
        }
        return id != null && id.equals(((Actor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Actor{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", apellidos='" + getApellidos() + "'" +
            "}";
    }
}
