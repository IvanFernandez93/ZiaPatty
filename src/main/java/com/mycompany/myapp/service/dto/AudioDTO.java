package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.Stato;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Audio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AudioDTO implements Serializable {

    private Long idAudio;

    private String nome;

    private Stato codiceStato;

    @Lob
    private byte[] track;

    private String trackContentType;
    private ZonedDateTime dataCreazione;

    private ZonedDateTime dataUltimaModifica;

    private Boolean eliminato;

    public Long getIdAudio() {
        return idAudio;
    }

    public void setIdAudio(Long idAudio) {
        this.idAudio = idAudio;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stato getCodiceStato() {
        return codiceStato;
    }

    public void setCodiceStato(Stato codiceStato) {
        this.codiceStato = codiceStato;
    }

    public byte[] getTrack() {
        return track;
    }

    public void setTrack(byte[] track) {
        this.track = track;
    }

    public String getTrackContentType() {
        return trackContentType;
    }

    public void setTrackContentType(String trackContentType) {
        this.trackContentType = trackContentType;
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
        if (!(o instanceof AudioDTO)) {
            return false;
        }

        AudioDTO audioDTO = (AudioDTO) o;
        if (this.idAudio == null) {
            return false;
        }
        return Objects.equals(this.idAudio, audioDTO.idAudio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idAudio);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AudioDTO{" +
            "idAudio=" + getIdAudio() +
            ", nome='" + getNome() + "'" +
            ", codiceStato='" + getCodiceStato() + "'" +
            ", track='" + getTrack() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            "}";
    }
}
