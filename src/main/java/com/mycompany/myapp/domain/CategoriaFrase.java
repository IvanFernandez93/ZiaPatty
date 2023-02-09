package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CategoriaFrase.
 */
@Entity
@Table(name = "zp_catgoria_frase")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoriaFrase implements Serializable {

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
    @JsonIgnoreProperties(value = { "padres", "categoriaFrases", "categorieFiglie" }, allowSetters = true)
    private Categoria categoria;

    @ManyToOne
    @JsonIgnoreProperties(value = { "fraseCategorias", "listaAudios", "lingua" }, allowSetters = true)
    private Frase frase;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoriaFrase id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCategoria() {
        return this.idCategoria;
    }

    public CategoriaFrase idCategoria(Long idCategoria) {
        this.setIdCategoria(idCategoria);
        return this;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Long getIdFrase() {
        return this.idFrase;
    }

    public CategoriaFrase idFrase(Long idFrase) {
        this.setIdFrase(idFrase);
        return this;
    }

    public void setIdFrase(Long idFrase) {
        this.idFrase = idFrase;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public CategoriaFrase categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public Frase getFrase() {
        return this.frase;
    }

    public void setFrase(Frase frase) {
        this.frase = frase;
    }

    public CategoriaFrase frase(Frase frase) {
        this.setFrase(frase);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoriaFrase)) {
            return false;
        }
        return id != null && id.equals(((CategoriaFrase) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaFrase{" +
            "id=" + getId() +
            ", idCategoria=" + getIdCategoria() +
            ", idFrase=" + getIdFrase() +
            "}";
    }
}
