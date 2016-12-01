package com.noturingback.tradinglearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.noturingback.tradinglearning.domain.enumeration.Categorie;

/**
 * A Prestation.
 */
@Entity
@Table(name = "prestation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "prestation")
public class Prestation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date_debut_livraison")
    private ZonedDateTime dateDebutLivraison;

    @Column(name = "date_fin_livraison")
    private ZonedDateTime dateFinLivraison;

    @Enumerated(EnumType.STRING)
    @Column(name = "categorie")
    private Categorie categorie;

    @Column(name = "titre")
    private String titre;

    @Column(name = "description")
    private String description;

    @Column(name = "nb_beneficiaires_max")
    private Integer nbBeneficiairesMax;

    @Column(name = "prix")
    private Integer prix;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "demande")
    private Boolean demande;

    @Column(name = "effectue")
    private Boolean effectue;

    @ManyToOne
    private Utilisateur auteur;

    @ManyToOne
    private Utilisateur demandeur;

    @ManyToOne
    private Utilisateur offreur;

    @ManyToMany(mappedBy = "servicesRecuses")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Utilisateur> beneficiaires = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDateDebutLivraison() {
        return dateDebutLivraison;
    }

    public Prestation dateDebutLivraison(ZonedDateTime dateDebutLivraison) {
        this.dateDebutLivraison = dateDebutLivraison;
        return this;
    }

    public void setDateDebutLivraison(ZonedDateTime dateDebutLivraison) {
        this.dateDebutLivraison = dateDebutLivraison;
    }

    public ZonedDateTime getDateFinLivraison() {
        return dateFinLivraison;
    }

    public Prestation dateFinLivraison(ZonedDateTime dateFinLivraison) {
        this.dateFinLivraison = dateFinLivraison;
        return this;
    }

    public void setDateFinLivraison(ZonedDateTime dateFinLivraison) {
        this.dateFinLivraison = dateFinLivraison;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Prestation categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public String getTitre() {
        return titre;
    }

    public Prestation titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public Prestation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNbBeneficiairesMax() {
        return nbBeneficiairesMax;
    }

    public Prestation nbBeneficiairesMax(Integer nbBeneficiairesMax) {
        this.nbBeneficiairesMax = nbBeneficiairesMax;
        return this;
    }

    public void setNbBeneficiairesMax(Integer nbBeneficiairesMax) {
        this.nbBeneficiairesMax = nbBeneficiairesMax;
    }

    public Integer getPrix() {
        return prix;
    }

    public Prestation prix(Integer prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Integer prix) {
        this.prix = prix;
    }

    public String getLieu() {
        return lieu;
    }

    public Prestation lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Boolean isDemande() {
        return demande;
    }

    public Prestation demande(Boolean demande) {
        this.demande = demande;
        return this;
    }

    public void setDemande(Boolean demande) {
        this.demande = demande;
    }

    public Boolean isEffectue() {
        return effectue;
    }

    public Prestation effectue(Boolean effectue) {
        this.effectue = effectue;
        return this;
    }

    public void setEffectue(Boolean effectue) {
        this.effectue = effectue;
    }

    public Utilisateur getAuteur() {
        return auteur;
    }

    public Prestation auteur(Utilisateur utilisateur) {
        this.auteur = utilisateur;
        return this;
    }

    public void setAuteur(Utilisateur utilisateur) {
        this.auteur = utilisateur;
    }

    public Utilisateur getDemandeur() {
        return demandeur;
    }

    public Prestation demandeur(Utilisateur utilisateur) {
        this.demandeur = utilisateur;
        return this;
    }

    public void setDemandeur(Utilisateur utilisateur) {
        this.demandeur = utilisateur;
    }

    public Utilisateur getOffreur() {
        return offreur;
    }

    public Prestation offreur(Utilisateur utilisateur) {
        this.offreur = utilisateur;
        return this;
    }

    public void setOffreur(Utilisateur utilisateur) {
        this.offreur = utilisateur;
    }

    public Set<Utilisateur> getBeneficiaires() {
        return beneficiaires;
    }

    public Prestation beneficiaires(Set<Utilisateur> utilisateurs) {
        this.beneficiaires = utilisateurs;
        return this;
    }

    public Prestation addBeneficiaires(Utilisateur utilisateur) {
        beneficiaires.add(utilisateur);
        utilisateur.getServicesRecuses().add(this);
        return this;
    }

    public Prestation removeBeneficiaires(Utilisateur utilisateur) {
        beneficiaires.remove(utilisateur);
        utilisateur.getServicesRecuses().remove(this);
        return this;
    }

    public void setBeneficiaires(Set<Utilisateur> utilisateurs) {
        this.beneficiaires = utilisateurs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prestation prestation = (Prestation) o;
        if (prestation.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, prestation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Prestation{" +
            "id=" + id +
            ", dateDebutLivraison='" + dateDebutLivraison + "'" +
            ", dateFinLivraison='" + dateFinLivraison + "'" +
            ", categorie='" + categorie + "'" +
            ", titre='" + titre + "'" +
            ", description='" + description + "'" +
            ", nbBeneficiairesMax='" + nbBeneficiairesMax + "'" +
            ", prix='" + prix + "'" +
            ", lieu='" + lieu + "'" +
            ", demande='" + demande + "'" +
            ", effectue='" + effectue + "'" +
            '}';
    }
}
