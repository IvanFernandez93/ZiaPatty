package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Stato;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Frase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraseDTO implements Serializable {

    private Long id;

    private Long idFrase;

    private String frase;

    private Stato codiceStato;

    private ZonedDateTime dataCreazione;

    private ZonedDateTime dataUltimaModifica;

    private Boolean eliminato;

    private LinguaDTO lingua;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFrase() {
        return idFrase;
    }

    public void setIdFrase(Long idFrase) {
        this.idFrase = idFrase;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
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

    public LinguaDTO getLingua() {
        return lingua;
    }

    public void setLingua(LinguaDTO lingua) {
        this.lingua = lingua;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FraseDTO)) {
            return false;
        }

        FraseDTO fraseDTO = (FraseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fraseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraseDTO{" +
            "id=" + getId() +
            ", idFrase=" + getIdFrase() +
            ", frase='" + getFrase() + "'" +
            ", codiceStato='" + getCodiceStato() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            ", lingua=" + getLingua() +
            "}";
    }
}
