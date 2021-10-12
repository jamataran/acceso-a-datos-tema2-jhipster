package com.cev.ad.tema2.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cev.ad.tema2.domain.Estreno} entity. This class is used
 * in {@link com.cev.ad.tema2.web.rest.EstrenoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /estrenos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EstrenoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter estreno;

    private LocalDateFilter fechaEstreno;

    private LongFilter peliculaId;

    private Boolean distinct;

    public EstrenoCriteria() {}

    public EstrenoCriteria(EstrenoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.estreno = other.estreno == null ? null : other.estreno.copy();
        this.fechaEstreno = other.fechaEstreno == null ? null : other.fechaEstreno.copy();
        this.peliculaId = other.peliculaId == null ? null : other.peliculaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public EstrenoCriteria copy() {
        return new EstrenoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEstreno() {
        return estreno;
    }

    public StringFilter estreno() {
        if (estreno == null) {
            estreno = new StringFilter();
        }
        return estreno;
    }

    public void setEstreno(StringFilter estreno) {
        this.estreno = estreno;
    }

    public LocalDateFilter getFechaEstreno() {
        return fechaEstreno;
    }

    public LocalDateFilter fechaEstreno() {
        if (fechaEstreno == null) {
            fechaEstreno = new LocalDateFilter();
        }
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDateFilter fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    public LongFilter getPeliculaId() {
        return peliculaId;
    }

    public LongFilter peliculaId() {
        if (peliculaId == null) {
            peliculaId = new LongFilter();
        }
        return peliculaId;
    }

    public void setPeliculaId(LongFilter peliculaId) {
        this.peliculaId = peliculaId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EstrenoCriteria that = (EstrenoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(estreno, that.estreno) &&
            Objects.equals(fechaEstreno, that.fechaEstreno) &&
            Objects.equals(peliculaId, that.peliculaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, estreno, fechaEstreno, peliculaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstrenoCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (estreno != null ? "estreno=" + estreno + ", " : "") +
            (fechaEstreno != null ? "fechaEstreno=" + fechaEstreno + ", " : "") +
            (peliculaId != null ? "peliculaId=" + peliculaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
