package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Lingua} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LinguaDTO implements Serializable {

    private Long idLingua;

    private String codiceLingua;

    private String nomeLingua;

    private ZonedDateTime dataCreazione;

    private ZonedDateTime dataUltimaModifica;

    private Boolean eliminato;

    public Long getIdLingua() {
        return idLingua;
    }

    public void setIdLingua(Long idLingua) {
        this.idLingua = idLingua;
    }

    public String getCodiceLingua() {
        return codiceLingua;
    }

    public void setCodiceLingua(String codiceLingua) {
        this.codiceLingua = codiceLingua;
    }

    public String getNomeLingua() {
        return nomeLingua;
    }

    public void setNomeLingua(String nomeLingua) {
        this.nomeLingua = nomeLingua;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LinguaDTO)) {
            return false;
        }

        LinguaDTO linguaDTO = (LinguaDTO) o;
        if (this.idLingua == null) {
            return false;
        }
        return Objects.equals(this.idLingua, linguaDTO.idLingua);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idLingua);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LinguaDTO{" +
            "idLingua=" + getIdLingua() +
            ", codiceLingua='" + getCodiceLingua() + "'" +
            ", nomeLingua='" + getNomeLingua() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            "}";
    }
}
