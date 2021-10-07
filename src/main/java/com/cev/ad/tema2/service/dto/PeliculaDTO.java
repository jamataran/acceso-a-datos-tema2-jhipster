package com.cev.ad.tema2.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.cev.ad.tema2.domain.Pelicula} entity.
 */
public class PeliculaDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 10, max = 250)
    private String titulo;

    @NotNull
    @Size(min = 10)
    private String descripcion;

    private Boolean enCines;

    private EstrenoDTO estreno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEnCines() {
        return enCines;
    }

    public void setEnCines(Boolean enCines) {
        this.enCines = enCines;
    }

    public EstrenoDTO getEstreno() {
        return estreno;
    }

    public void setEstreno(EstrenoDTO estreno) {
        this.estreno = estreno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PeliculaDTO)) {
            return false;
        }

        PeliculaDTO peliculaDTO = (PeliculaDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, peliculaDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PeliculaDTO{" +
            "id=" + getId() +
            ", titulo='" + getTitulo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", enCines='" + getEnCines() + "'" +
            ", estreno=" + getEstreno() +
            "}";
    }
}
