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
 * A Frase.
 */
@Entity
@Table(name = "zp_frase")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Frase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_frase")
    private Long idFrase;

    @Column(name = "frase")
    private String frase;

    @Enumerated(EnumType.STRING)
    @Column(name = "codice_stato")
    private Stato codiceStato;

    @Column(name = "data_creazione")
    private ZonedDateTime dataCreazione;

    @Column(name = "data_ultima_modifica")
    private ZonedDateTime dataUltimaModifica;

    @Column(name = "eliminato")
    private Boolean eliminato;

    @OneToMany(mappedBy = "frase")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categoria", "frase" }, allowSetters = true)
    private Set<CategoriaFrase> fraseCategorias = new HashSet<>();

    @OneToMany(mappedBy = "frase")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "frase", "audio" }, allowSetters = true)
    private Set<FraseAudio> listaAudios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "listaFrasis" }, allowSetters = true)
    private Lingua lingua;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Frase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdFrase() {
        return this.idFrase;
    }

    public Frase idFrase(Long idFrase) {
        this.setIdFrase(idFrase);
        return this;
    }

    public void setIdFrase(Long idFrase) {
        this.idFrase = idFrase;
    }

    public String getFrase() {
        return this.frase;
    }

    public Frase frase(String frase) {
        this.setFrase(frase);
        return this;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public Stato getCodiceStato() {
        return this.codiceStato;
    }

    public Frase codiceStato(Stato codiceStato) {
        this.setCodiceStato(codiceStato);
        return this;
    }

    public void setCodiceStato(Stato codiceStato) {
        this.codiceStato = codiceStato;
    }

    public ZonedDateTime getDataCreazione() {
        return this.dataCreazione;
    }

    public Frase dataCreazione(ZonedDateTime dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(ZonedDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public ZonedDateTime getDataUltimaModifica() {
        return this.dataUltimaModifica;
    }

    public Frase dataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.setDataUltimaModifica(dataUltimaModifica);
        return this;
    }

    public void setDataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public Boolean getEliminato() {
        return this.eliminato;
    }

    public Frase eliminato(Boolean eliminato) {
        this.setEliminato(eliminato);
        return this;
    }

    public void setEliminato(Boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Set<CategoriaFrase> getFraseCategorias() {
        return this.fraseCategorias;
    }

    public void setFraseCategorias(Set<CategoriaFrase> categoriaFrases) {
        if (this.fraseCategorias != null) {
            this.fraseCategorias.forEach(i -> i.setFrase(null));
        }
        if (categoriaFrases != null) {
            categoriaFrases.forEach(i -> i.setFrase(this));
        }
        this.fraseCategorias = categoriaFrases;
    }

    public Frase fraseCategorias(Set<CategoriaFrase> categoriaFrases) {
        this.setFraseCategorias(categoriaFrases);
        return this;
    }

    public Frase addFraseCategoria(CategoriaFrase categoriaFrase) {
        this.fraseCategorias.add(categoriaFrase);
        categoriaFrase.setFrase(this);
        return this;
    }

    public Frase removeFraseCategoria(CategoriaFrase categoriaFrase) {
        this.fraseCategorias.remove(categoriaFrase);
        categoriaFrase.setFrase(null);
        return this;
    }

    public Set<FraseAudio> getListaAudios() {
        return this.listaAudios;
    }

    public void setListaAudios(Set<FraseAudio> fraseAudios) {
        if (this.listaAudios != null) {
            this.listaAudios.forEach(i -> i.setFrase(null));
        }
        if (fraseAudios != null) {
            fraseAudios.forEach(i -> i.setFrase(this));
        }
        this.listaAudios = fraseAudios;
    }

    public Frase listaAudios(Set<FraseAudio> fraseAudios) {
        this.setListaAudios(fraseAudios);
        return this;
    }

    public Frase addListaAudio(FraseAudio fraseAudio) {
        this.listaAudios.add(fraseAudio);
        fraseAudio.setFrase(this);
        return this;
    }

    public Frase removeListaAudio(FraseAudio fraseAudio) {
        this.listaAudios.remove(fraseAudio);
        fraseAudio.setFrase(null);
        return this;
    }

    public Lingua getLingua() {
        return this.lingua;
    }

    public void setLingua(Lingua lingua) {
        this.lingua = lingua;
    }

    public Frase lingua(Lingua lingua) {
        this.setLingua(lingua);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Frase)) {
            return false;
        }
        return id != null && id.equals(((Frase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Frase{" +
            "id=" + getId() +
            ", idFrase=" + getIdFrase() +
            ", frase='" + getFrase() + "'" +
            ", codiceStato='" + getCodiceStato() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            "}";
    }
}
