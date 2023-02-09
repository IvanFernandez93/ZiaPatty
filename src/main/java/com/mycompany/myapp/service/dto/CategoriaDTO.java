package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Stato;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Categoria} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaDTO implements Serializable {

    private Long idCategoria;

    private String nome;

    private Long idCategoriaPadre;

    private Stato codiceStato;

    private ZonedDateTime dataCreazione;

    private ZonedDateTime dataUltimaModifica;

    private Boolean eliminato;

    private CategoriaDTO categorieFiglie;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdCategoriaPadre() {
        return idCategoriaPadre;
    }

    public void setIdCategoriaPadre(Long idCategoriaPadre) {
        this.idCategoriaPadre = idCategoriaPadre;
    }

    public Stato getCodiceStato() {
        return codiceStato;
    }

    public void setCodiceStato(Stato codiceStato) {
        this.codiceStato = codiceStato;
    }

    public ZonedDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(ZonedDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public ZonedDateTime getDataUltimaModifica() {
        return dataUltimaModifica;
    }

    public void setDataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public Boolean getEliminato() {
        return eliminato;
    }

    public void setEliminato(Boolean eliminato) {
        this.eliminato = eliminato;
    }

    public CategoriaDTO getCategorieFiglie() {
        return categorieFiglie;
    }

    public void setCategorieFiglie(CategoriaDTO categorieFiglie) {
        this.categorieFiglie = categorieFiglie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaDTO)) {
            return false;
        }

        CategoriaDTO categoriaDTO = (CategoriaDTO) o;
        if (this.idCategoria == null) {
            return false;
        }
        return Objects.equals(this.idCategoria, categoriaDTO.idCategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idCategoria);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaDTO{" +
            "idCategoria=" + getIdCategoria() +
            ", nome='" + getNome() + "'" +
            ", idCategoriaPadre=" + getIdCategoriaPadre() +
            ", codiceStato='" + getCodiceStato() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            ", categorieFiglie=" + getCategorieFiglie() +
            "}";
    }
}
