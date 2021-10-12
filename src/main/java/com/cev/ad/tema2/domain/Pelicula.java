package com.cev.ad.tema2.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 10, max = 250)
    @Column(name = "titulo", length = 250, nullable = false)
    private String titulo;

    @NotNull
    @Size(min = 10)
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "en_cines")
    private Boolean enCines;

    @JsonIgnoreProperties(value = { "pelicula" }, allowSetters = true)
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Estreno estreno;

    @OneToMany(mappedBy = "pelicula")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pelicula" }, allowSetters = true)
    private Set<Review> reviews = new HashSet<>();

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

    public Estreno getEstreno() {
        return this.estreno;
    }

    public void setEstreno(Estreno estreno) {
        this.estreno = estreno;
    }

    public Pelicula estreno(Estreno estreno) {
        this.setEstreno(estreno);
        return this;
    }

    public Set<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(Set<Review> reviews) {
        if (this.reviews != null) {
            this.reviews.forEach(i -> i.setPelicula(null));
        }
        if (reviews != null) {
            reviews.forEach(i -> i.setPelicula(this));
        }
        this.reviews = reviews;
    }

    public Pelicula reviews(Set<Review> reviews) {
        this.setReviews(reviews);
        return this;
    }

    public Pelicula addReview(Review review) {
        this.reviews.add(review);
        review.setPelicula(this);
        return this;
    }

    public Pelicula removeReview(Review review) {
        this.reviews.remove(review);
        review.setPelicula(null);
        return this;
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
            ", descripcion='" + getDescripcion() + "'" +
            ", enCines='" + getEnCines() + "'" +
            "}";
    }
}
