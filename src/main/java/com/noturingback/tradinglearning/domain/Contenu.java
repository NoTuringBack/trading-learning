package com.noturingback.tradinglearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.noturingback.tradinglearning.domain.enumeration.Type;

import com.noturingback.tradinglearning.domain.enumeration.Categorie;

/**
 * A Contenu.
 */
@Entity
@Table(name = "contenu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contenu")
public class Contenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private Categorie categorie;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "donnees")
    private byte[] donnees;

    @Column(name = "donnees_content_type")
    private String donneesContentType;

    @Column(name = "prix")
    private Integer prix;

    @ManyToOne
    private Utilisateur createur;

    @ManyToMany(mappedBy = "contenusConsultes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Utilisateur> utilisateursConsultants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public Contenu dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Type getType() {
        return type;
    }

    public Contenu type(Type type) {
        this.type = type;
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Contenu categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getTitre() {
        return titre;
    }

    public Contenu titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public Contenu description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getDonnees() {
        return donnees;
    }

    public Contenu donnees(byte[] donnees) {
        this.donnees = donnees;
        return this;
    }

    public void setDonnees(byte[] donnees) {
        this.donnees = donnees;
    }

    public String getDonneesContentType() {
        return donneesContentType;
    }

    public Contenu donneesContentType(String donneesContentType) {
        this.donneesContentType = donneesContentType;
        return this;
    }

    public void setDonneesContentType(String donneesContentType) {
        this.donneesContentType = donneesContentType;
    }

    public Integer getPrix() {
        return prix;
    }

    public Contenu prix(Integer prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public Utilisateur getCreateur() {
        return createur;
    }

    public Contenu createur(Utilisateur utilisateur) {
        this.createur = utilisateur;
        return this;
    }

    public void setCreateur(Utilisateur utilisateur) {
        this.createur = utilisateur;
    }

    public Set<Utilisateur> getUtilisateursConsultants() {
        return utilisateursConsultants;
    }

    public Contenu utilisateursConsultants(Set<Utilisateur> utilisateurs) {
        this.utilisateursConsultants = utilisateurs;
        return this;
    }

    public Contenu addUtilisateursConsultant(Utilisateur utilisateur) {
        utilisateursConsultants.add(utilisateur);
        utilisateur.getContenusConsultes().add(this);
        return this;
    }

    public Contenu removeUtilisateursConsultant(Utilisateur utilisateur) {
        utilisateursConsultants.remove(utilisateur);
        utilisateur.getContenusConsultes().remove(this);
        return this;
    }

    public void setUtilisateursConsultants(Set<Utilisateur> utilisateurs) {
        this.utilisateursConsultants = utilisateurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contenu contenu = (Contenu) o;
        if (contenu.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contenu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contenu{" +
            "id=" + id +
            ", dateCreation='" + dateCreation + "'" +
            ", type='" + type + "'" +
            ", categorie='" + categorie + "'" +
            ", titre='" + titre + "'" +
            ", description='" + description + "'" +
            ", donnees='" + donnees + "'" +
            ", donneesContentType='" + donneesContentType + "'" +
            ", prix='" + prix + "'" +
            '}';
    }
}
