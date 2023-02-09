package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Stato;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Audio.
 */
@Entity
@Table(name = "zp_audio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Audio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id_audio")
    private Long idAudio;

    @Column(name = "nome")
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "codice_stato")
    private Stato codiceStato;

    @Lob
    @Column(name = "track")
    private byte[] track;

    @Column(name = "track_content_type")
    private String trackContentType;

    @Column(name = "data_creazione")
    private ZonedDateTime dataCreazione;

    @Column(name = "data_ultima_modifica")
    private ZonedDateTime dataUltimaModifica;

    @Column(name = "eliminato")
    private Boolean eliminato;

    @OneToMany(mappedBy = "audio")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "frase", "audio" }, allowSetters = true)
    private Set<FraseAudio> listaFrasis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getIdAudio() {
        return this.idAudio;
    }

    public Audio idAudio(Long idAudio) {
        this.setIdAudio(idAudio);
        return this;
    }

    public void setIdAudio(Long idAudio) {
        this.idAudio = idAudio;
    }

    public String getNome() {
        return this.nome;
    }

    public Audio nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Stato getCodiceStato() {
        return this.codiceStato;
    }

    public Audio codiceStato(Stato codiceStato) {
        this.setCodiceStato(codiceStato);
        return this;
    }

    public void setCodiceStato(Stato codiceStato) {
        this.codiceStato = codiceStato;
    }

    public byte[] getTrack() {
        return this.track;
    }

    public Audio track(byte[] track) {
        this.setTrack(track);
        return this;
    }

    public void setTrack(byte[] track) {
        this.track = track;
    }

    public String getTrackContentType() {
        return this.trackContentType;
    }

    public Audio trackContentType(String trackContentType) {
        this.trackContentType = trackContentType;
        return this;
    }

    public void setTrackContentType(String trackContentType) {
        this.trackContentType = trackContentType;
    }

    public ZonedDateTime getDataCreazione() {
        return this.dataCreazione;
    }

    public Audio dataCreazione(ZonedDateTime dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(ZonedDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public ZonedDateTime getDataUltimaModifica() {
        return this.dataUltimaModifica;
    }

    public Audio dataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.setDataUltimaModifica(dataUltimaModifica);
        return this;
    }

    public void setDataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public Boolean getEliminato() {
        return this.eliminato;
    }

    public Audio eliminato(Boolean eliminato) {
        this.setEliminato(eliminato);
        return this;
    }

    public void setEliminato(Boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Set<FraseAudio> getListaFrasis() {
        return this.listaFrasis;
    }

    public void setListaFrasis(Set<FraseAudio> fraseAudios) {
        if (this.listaFrasis != null) {
            this.listaFrasis.forEach(i -> i.setAudio(null));
        }
        if (fraseAudios != null) {
            fraseAudios.forEach(i -> i.setAudio(this));
        }
        this.listaFrasis = fraseAudios;
    }

    public Audio listaFrasis(Set<FraseAudio> fraseAudios) {
        this.setListaFrasis(fraseAudios);
        return this;
    }

    public Audio addListaFrasi(FraseAudio fraseAudio) {
        this.listaFrasis.add(fraseAudio);
        fraseAudio.setAudio(this);
        return this;
    }

    public Audio removeListaFrasi(FraseAudio fraseAudio) {
        this.listaFrasis.remove(fraseAudio);
        fraseAudio.setAudio(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Audio)) {
            return false;
        }
        return idAudio != null && idAudio.equals(((Audio) o).idAudio);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Audio{" +
            "idAudio=" + getIdAudio() +
            ", nome='" + getNome() + "'" +
            ", codiceStato='" + getCodiceStato() + "'" +
            ", track='" + getTrack() + "'" +
            ", trackContentType='" + getTrackContentType() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            "}";
    }
}
