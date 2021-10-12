package com.cev.ad.tema2.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.cev.ad.tema2.domain.Actor} entity. This class is used
 * in {@link com.cev.ad.tema2.web.rest.ActorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /actors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter bio;

    private LongFilter peliculaId;

    private Boolean distinct;

    public ActorCriteria() {}

    public ActorCriteria(ActorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.bio = other.bio == null ? null : other.bio.copy();
        this.peliculaId = other.peliculaId == null ? null : other.peliculaId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ActorCriteria copy() {
        return new ActorCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getBio() {
        return bio;
    }

    public StringFilter bio() {
        if (bio == null) {
            bio = new StringFilter();
        }
        return bio;
    }

    public void setBio(StringFilter bio) {
        this.bio = bio;
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
        final ActorCriteria that = (ActorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(bio, that.bio) &&
            Objects.equals(peliculaId, that.peliculaId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, bio, peliculaId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActorCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (bio != null ? "bio=" + bio + ", " : "") +
            (peliculaId != null ? "peliculaId=" + peliculaId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
