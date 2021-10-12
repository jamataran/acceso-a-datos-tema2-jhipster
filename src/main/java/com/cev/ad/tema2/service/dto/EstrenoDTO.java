package com.cev.ad.tema2.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.cev.ad.tema2.domain.Estreno} entity.
 */
public class EstrenoDTO implements Serializable {

    private Long id;

    private String estreno;

    private LocalDate fechaEstreno;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstreno() {
        return estreno;
    }

    public void setEstreno(String estreno) {
        this.estreno = estreno;
    }

    public LocalDate getFechaEstreno() {
        return fechaEstreno;
    }

    public void setFechaEstreno(LocalDate fechaEstreno) {
        this.fechaEstreno = fechaEstreno;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EstrenoDTO)) {
            return false;
        }

        EstrenoDTO estrenoDTO = (EstrenoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, estrenoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EstrenoDTO{" +
            "id=" + getId() +
            ", estreno='" + getEstreno() + "'" +
            ", fechaEstreno='" + getFechaEstreno() + "'" +
            "}";
    }
}
