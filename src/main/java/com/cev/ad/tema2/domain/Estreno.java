package com.cev.ad.tema2.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Estreno.
 */
@Entity
@Table(name = "estreno")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "estreno")
public class Estreno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "estreno")
    private String estreno;

    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estreno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstreno() {
        return this.estreno;
    }

    public Estreno estreno(String estreno) {
        this.setEstreno(estreno);
        return this;
    }

    public void setEstreno(String estreno) {
        this.estreno = estreno;
    }

    public LocalDate getFechaEstreno() {
        return this.fechaEstreno;
    }

    public Estreno fechaEstreno(LocalDate fechaEstreno) {
        this.setFechaEstreno(fechaEstreno);
        return this;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estreno)) {
            return false;
        }
        return id != null && id.equals(((Estreno) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estreno{" +
            "id=" + getId() +
            ", estreno='" + getEstreno() + "'" +
            ", fechaEstreno='" + getFechaEstreno() + "'" +
            "}";
    }
}
