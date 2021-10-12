package com.cev.ad.tema2.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.cev.ad.tema2.domain.Actor} entity.
 */
public class ActorDTO implements Serializable {

    private Long id;

    private String nombre;

    private String bio;

    private Set<PeliculaDTO> peliculas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Set<PeliculaDTO> getPeliculas() {
        return peliculas;
    }

    public void setPeliculas(Set<PeliculaDTO> peliculas) {
        this.peliculas = peliculas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActorDTO)) {
            return false;
        }

        ActorDTO actorDTO = (ActorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActorDTO{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", bio='" + getBio() + "'" +
            ", peliculas=" + getPeliculas() +
            "}";
    }
}
