package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FraseAudio.
 */
@Entity
@Table(name = "zp_frase_audio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FraseAudio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "id_frase")
    private Long idFrase;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fraseCategorias", "listaAudios", "lingua" }, allowSetters = true)
    private Frase frase;

    @ManyToOne
    @JsonIgnoreProperties(value = { "listaFrasis" }, allowSetters = true)
    private Audio audio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FraseAudio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCategoria() {
        return this.idCategoria;
    }

    public FraseAudio idCategoria(Long idCategoria) {
        this.setIdCategoria(idCategoria);
        return this;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Long getIdFrase() {
        return this.idFrase;
    }

    public FraseAudio idFrase(Long idFrase) {
        this.setIdFrase(idFrase);
        return this;
    }

    public void setIdFrase(Long idFrase) {
        this.idFrase = idFrase;
    }

    public Frase getFrase() {
        return this.frase;
    }

    public void setFrase(Frase frase) {
        this.frase = frase;
    }

    public FraseAudio frase(Frase frase) {
        this.setFrase(frase);
        return this;
    }

    public Audio getAudio() {
        return this.audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public FraseAudio audio(Audio audio) {
        this.setAudio(audio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FraseAudio)) {
            return false;
        }
        return id != null && id.equals(((FraseAudio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FraseAudio{" +
            "id=" + getId() +
            ", idCategoria=" + getIdCategoria() +
            ", idFrase=" + getIdFrase() +
            "}";
    }
}
