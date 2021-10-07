package com.cev.ad.tema2.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cev.ad.tema2.domain.Review} entity. This class is used
 * in {@link com.cev.ad.tema2.web.rest.ReviewResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /reviews?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ReviewCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter puntuacion;

    private StringFilter descripcion;

    private LongFilter peliculaId;

    private Boolean distinct;

    public ReviewCriteria() {}

    public ReviewCriteria(ReviewCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.puntuacion = other.puntuacion == null ? null : other.puntuacion.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.peliculaId = other.peliculaId == null ? null : other.peliculaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ReviewCriteria copy() {
        return new ReviewCriteria(this);
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

    public IntegerFilter getPuntuacion() {
        return puntuacion;
    }

    public IntegerFilter puntuacion() {
        if (puntuacion == null) {
            puntuacion = new IntegerFilter();
        }
        return puntuacion;
    }

    public void setPuntuacion(IntegerFilter puntuacion) {
        this.puntuacion = puntuacion;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
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
        final ReviewCriteria that = (ReviewCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(puntuacion, that.puntuacion) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(peliculaId, that.peliculaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, puntuacion, descripcion, peliculaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReviewCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (puntuacion != null ? "puntuacion=" + puntuacion + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (peliculaId != null ? "peliculaId=" + peliculaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
