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
 * A Categoria.
 */
@Entity
@Table(name = "zp_categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(name = "nome")
    private String nome;

    @Column(name = "id_categoria_padre")
    private Long idCategoriaPadre;

    @Enumerated(EnumType.STRING)
    @Column(name = "codice_stato")
    private Stato codiceStato;

    @Column(name = "data_creazione")
    private ZonedDateTime dataCreazione;

    @Column(name = "data_ultima_modifica")
    private ZonedDateTime dataUltimaModifica;

    @Column(name = "eliminato")
    private Boolean eliminato;

    @OneToMany(mappedBy = "categorieFiglie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "padres", "categoriaFrases", "categorieFiglie" }, allowSetters = true)
    private Set<Categoria> padres = new HashSet<>();

    @OneToMany(mappedBy = "categoria")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "categoria", "frase" }, allowSetters = true)
    private Set<CategoriaFrase> categoriaFrases = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "padres", "categoriaFrases", "categorieFiglie" }, allowSetters = true)
    private Categoria categorieFiglie;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getIdCategoria() {
        return this.idCategoria;
    }

    public Categoria idCategoria(Long idCategoria) {
        this.setIdCategoria(idCategoria);
        return this;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return this.nome;
    }

    public Categoria nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getIdCategoriaPadre() {
        return this.idCategoriaPadre;
    }

    public Categoria idCategoriaPadre(Long idCategoriaPadre) {
        this.setIdCategoriaPadre(idCategoriaPadre);
        return this;
    }

    public void setIdCategoriaPadre(Long idCategoriaPadre) {
        this.idCategoriaPadre = idCategoriaPadre;
    }

    public Stato getCodiceStato() {
        return this.codiceStato;
    }

    public Categoria codiceStato(Stato codiceStato) {
        this.setCodiceStato(codiceStato);
        return this;
    }

    public void setCodiceStato(Stato codiceStato) {
        this.codiceStato = codiceStato;
    }

    public ZonedDateTime getDataCreazione() {
        return this.dataCreazione;
    }

    public Categoria dataCreazione(ZonedDateTime dataCreazione) {
        this.setDataCreazione(dataCreazione);
        return this;
    }

    public void setDataCreazione(ZonedDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public ZonedDateTime getDataUltimaModifica() {
        return this.dataUltimaModifica;
    }

    public Categoria dataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.setDataUltimaModifica(dataUltimaModifica);
        return this;
    }

    public void setDataUltimaModifica(ZonedDateTime dataUltimaModifica) {
        this.dataUltimaModifica = dataUltimaModifica;
    }

    public Boolean getEliminato() {
        return this.eliminato;
    }

    public Categoria eliminato(Boolean eliminato) {
        this.setEliminato(eliminato);
        return this;
    }

    public void setEliminato(Boolean eliminato) {
        this.eliminato = eliminato;
    }

    public Set<Categoria> getPadres() {
        return this.padres;
    }

    public void setPadres(Set<Categoria> categorias) {
        if (this.padres != null) {
            this.padres.forEach(i -> i.setCategorieFiglie(null));
        }
        if (categorias != null) {
            categorias.forEach(i -> i.setCategorieFiglie(this));
        }
        this.padres = categorias;
    }

    public Categoria padres(Set<Categoria> categorias) {
        this.setPadres(categorias);
        return this;
    }

    public Categoria addPadre(Categoria categoria) {
        this.padres.add(categoria);
        categoria.setCategorieFiglie(this);
        return this;
    }

    public Categoria removePadre(Categoria categoria) {
        this.padres.remove(categoria);
        categoria.setCategorieFiglie(null);
        return this;
    }

    public Set<CategoriaFrase> getCategoriaFrases() {
        return this.categoriaFrases;
    }

    public void setCategoriaFrases(Set<CategoriaFrase> categoriaFrases) {
        if (this.categoriaFrases != null) {
            this.categoriaFrases.forEach(i -> i.setCategoria(null));
        }
        if (categoriaFrases != null) {
            categoriaFrases.forEach(i -> i.setCategoria(this));
        }
        this.categoriaFrases = categoriaFrases;
    }

    public Categoria categoriaFrases(Set<CategoriaFrase> categoriaFrases) {
        this.setCategoriaFrases(categoriaFrases);
        return this;
    }

    public Categoria addCategoriaFrase(CategoriaFrase categoriaFrase) {
        this.categoriaFrases.add(categoriaFrase);
        categoriaFrase.setCategoria(this);
        return this;
    }

    public Categoria removeCategoriaFrase(CategoriaFrase categoriaFrase) {
        this.categoriaFrases.remove(categoriaFrase);
        categoriaFrase.setCategoria(null);
        return this;
    }

    public Categoria getCategorieFiglie() {
        return this.categorieFiglie;
    }

    public void setCategorieFiglie(Categoria categoria) {
        this.categorieFiglie = categoria;
    }

    public Categoria categorieFiglie(Categoria categoria) {
        this.setCategorieFiglie(categoria);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        return idCategoria != null && idCategoria.equals(((Categoria) o).idCategoria);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categoria{" +
            "idCategoria=" + getIdCategoria() +
            ", nome='" + getNome() + "'" +
            ", idCategoriaPadre=" + getIdCategoriaPadre() +
            ", codiceStato='" + getCodiceStato() + "'" +
            ", dataCreazione='" + getDataCreazione() + "'" +
            ", dataUltimaModifica='" + getDataUltimaModifica() + "'" +
            ", eliminato='" + getEliminato() + "'" +
            "}";
    }
}
