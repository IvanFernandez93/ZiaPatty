package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.FraseAudio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraseAudioDTO implements Serializable {

    private Long id;

    private Long idCategoria;

    private Long idFrase;

    private FraseDTO frase;

    private AudioDTO audio;

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

    public FraseDTO getFrase() {
        return frase;
    }

    public void setFrase(FraseDTO frase) {
        this.frase = frase;
    }

    public AudioDTO getAudio() {
        return audio;
    }

    public void setAudio(AudioDTO audio) {
        this.audio = audio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FraseAudioDTO)) {
            return false;
        }

        FraseAudioDTO fraseAudioDTO = (FraseAudioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, fraseAudioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraseAudioDTO{" +
            "id=" + getId() +
            ", idCategoria=" + getIdCategoria() +
            ", idFrase=" + getIdFrase() +
            ", frase=" + getFrase() +
            ", audio=" + getAudio() +
            "}";
    }
}
