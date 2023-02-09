package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CategoriaFrase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaFraseDTO implements Serializable {

    private Long id;

    private Long idCategoria;

    private Long idFrase;

    private CategoriaDTO categoria;

    private FraseDTO frase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Long getIdFrase() {
        return idFrase;
    }

    public void setIdFrase(Long idFrase) {
        this.idFrase = idFrase;
    }

    public CategoriaDTO getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDTO categoria) {
        this.categoria = categoria;
    }

    public FraseDTO getFrase() {
        return frase;
    }

    public void setFrase(FraseDTO frase) {
        this.frase = frase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaFraseDTO)) {
            return false;
        }

        CategoriaFraseDTO categoriaFraseDTO = (CategoriaFraseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, categoriaFraseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaFraseDTO{" +
            "id=" + getId() +
            ", idCategoria=" + getIdCategoria() +
            ", idFrase=" + getIdFrase() +
            ", categoria=" + getCategoria() +
            ", frase=" + getFrase() +
            "}";
    }
}
