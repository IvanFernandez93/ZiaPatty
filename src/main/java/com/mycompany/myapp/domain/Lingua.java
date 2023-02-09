package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lingua.
 */
@Entity
@Table(name = "zp_lingua")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lingua implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id_lingua")
    private Long idLingua;

    @Column(name = "codice_lingua")
    private String codiceLingua;

    @Column(name = "nome_lingua")
    private String nomeLingua;

    @Column(name = "data_creazione")
    private ZonedDateTime dataCreazione;

    @Column(name = "data_ultima_modifica")
    private ZonedDateTime dataUltimaModifica;

    @Column(name = "eliminato")
    private Boolean eliminato;

    @OneToMany(mappedBy = "lingua")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fraseCategorias", "listaAudios", "lingua" }, allowSetters = true)
    private Set<Frase> listaFrasis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getIdLingua() {
        return this.idLingua;
    }

    public Lingua idLingua(Long idLingua) {
        this.setIdLingua(idLingua);
        return this;
    }

    public void setIdLingua(Long idLingua) {
        this.idLingua = idLingua;
    }

    public String getCodiceLingua() {
        return this.codiceLingua;
    }

    public Lingua codiceLingua(String codiceLingua) {
        this.setCodiceLingua(codiceLingua);
        return this;
    }

    public void setCodiceLingua(String codiceLingua) {
        this.codiceLingua = codiceLingua;
    }

    public String getNomeLingua() {
        return this.nomeLingua;
    }

    public Lingua nomeLingua(String nomeLingua) {
        this.setNomeLingua(nomeLingua);
        return this;
    }

    public void setNomeLingua(String nomeLingua) {
        this.nomeLingua = nomeLingua;
    }

    public ZonedDateTime getDataCreazione() {
        return this.dataCreazione;
    }

    public Lingua dataCreazione(ZonedDateTime dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(ZonedDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public ZonedDateTime getDataUltimaModifica() {
        return this.dataUltimaModifica;
    }

    public Lingua dataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.setDataUltimaModifica(dataUltimaModifica);
        return this;
    }

    public void setDataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public Boolean getEliminato() {
        return this.eliminato;
    }

    public Lingua eliminato(Boolean eliminato) {
        this.setEliminato(eliminato);
        return this;
    }

    public void setEliminato(Boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Set<Frase> getListaFrasis() {
        return this.listaFrasis;
    }

    public void setListaFrasis(Set<Frase> frases) {
        if (this.listaFrasis != null) {
            this.listaFrasis.forEach(i -> i.setLingua(null));
        }
        if (frases != null) {
            frases.forEach(i -> i.setLingua(this));
        }
        this.listaFrasis = frases;
    }

    public Lingua listaFrasis(Set<Frase> frases) {
        this.setListaFrasis(frases);
        return this;
    }

    public Lingua addListaFrasi(Frase frase) {
        this.listaFrasis.add(frase);
        frase.setLingua(this);
        return this;
    }

    public Lingua removeListaFrasi(Frase frase) {
        this.listaFrasis.remove(frase);
        frase.setLingua(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lingua)) {
            return false;
        }
        return idLingua != null && idLingua.equals(((Lingua) o).idLingua);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lingua{" +
            "idLingua=" + getIdLingua() +
            ", codiceLingua='" + getCodiceLingua() + "'" +
            ", nomeLingua='" + getNomeLingua() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            "}";
    }
}
