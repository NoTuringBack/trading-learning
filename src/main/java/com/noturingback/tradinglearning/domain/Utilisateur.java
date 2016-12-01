package com.noturingback.tradinglearning.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * L'entite User de Jhipster, a laquelle un Utilisateur est lié, contient déjà les informations sur :- L'identifiant / mot de passe- Le nom / prénom- L'adresse email
 */
@ApiModel(description = "L'entite User de Jhipster, a laquelle un Utilisateur est lié, contient déjà les informations sur :- L'identifiant / mot de passe- Le nom / prénom- L'adresse email")
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "utilisateur")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "credits")
    private Integer credits;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "createur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Contenu> contenusPartages = new HashSet<>();

    @OneToMany(mappedBy = "auteur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prestation> servicesRenduses = new HashSet<>();

    @OneToMany(mappedBy = "demandeur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prestation> servicesDemandes = new HashSet<>();

    @OneToMany(mappedBy = "offreur")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Prestation> servicesProposes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "utilisateur_contenus_consultes",
               joinColumns = @JoinColumn(name="utilisateurs_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="contenus_consultes_id", referencedColumnName="ID"))
    private Set<Contenu> contenusConsultes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "utilisateur_services_recus",
               joinColumns = @JoinColumn(name="utilisateurs_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="services_recuses_id", referencedColumnName="ID"))
    private Set<Prestation> servicesRecuses = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCredits() {
        return credits;
    }

    public Utilisateur credits(Integer credits) {
        this.credits = credits;
        return this;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public User getUser() {
        return user;
    }

    public Utilisateur user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Contenu> getContenusPartages() {
        return contenusPartages;
    }

    public Utilisateur contenusPartages(Set<Contenu> contenus) {
        this.contenusPartages = contenus;
        return this;
    }

    public Utilisateur addContenusPartages(Contenu contenu) {
        contenusPartages.add(contenu);
        contenu.setCreateur(this);
        return this;
    }

    public Utilisateur removeContenusPartages(Contenu contenu) {
        contenusPartages.remove(contenu);
        contenu.setCreateur(null);
        return this;
    }

    public void setContenusPartages(Set<Contenu> contenus) {
        this.contenusPartages = contenus;
    }

    public Set<Prestation> getServicesRenduses() {
        return servicesRenduses;
    }

    public Utilisateur servicesRenduses(Set<Prestation> prestations) {
        this.servicesRenduses = prestations;
        return this;
    }

    public Utilisateur addServicesRendus(Prestation prestation) {
        servicesRenduses.add(prestation);
        prestation.setAuteur(this);
        return this;
    }

    public Utilisateur removeServicesRendus(Prestation prestation) {
        servicesRenduses.remove(prestation);
        prestation.setAuteur(null);
        return this;
    }

    public void setServicesRenduses(Set<Prestation> prestations) {
        this.servicesRenduses = prestations;
    }

    public Set<Prestation> getServicesDemandes() {
        return servicesDemandes;
    }

    public Utilisateur servicesDemandes(Set<Prestation> prestations) {
        this.servicesDemandes = prestations;
        return this;
    }

    public Utilisateur addServicesDemandes(Prestation prestation) {
        servicesDemandes.add(prestation);
        prestation.setDemandeur(this);
        return this;
    }

    public Utilisateur removeServicesDemandes(Prestation prestation) {
        servicesDemandes.remove(prestation);
        prestation.setDemandeur(null);
        return this;
    }

    public void setServicesDemandes(Set<Prestation> prestations) {
        this.servicesDemandes = prestations;
    }

    public Set<Prestation> getServicesProposes() {
        return servicesProposes;
    }

    public Utilisateur servicesProposes(Set<Prestation> prestations) {
        this.servicesProposes = prestations;
        return this;
    }

    public Utilisateur addServicesProposes(Prestation prestation) {
        servicesProposes.add(prestation);
        prestation.setOffreur(this);
        return this;
    }

    public Utilisateur removeServicesProposes(Prestation prestation) {
        servicesProposes.remove(prestation);
        prestation.setOffreur(null);
        return this;
    }

    public void setServicesProposes(Set<Prestation> prestations) {
        this.servicesProposes = prestations;
    }

    public Set<Contenu> getContenusConsultes() {
        return contenusConsultes;
    }

    public Utilisateur contenusConsultes(Set<Contenu> contenus) {
        this.contenusConsultes = contenus;
        return this;
    }

    public Utilisateur addContenusConsultes(Contenu contenu) {
        contenusConsultes.add(contenu);
        contenu.getUtilisateursConsultants().add(this);
        return this;
    }

    public Utilisateur removeContenusConsultes(Contenu contenu) {
        contenusConsultes.remove(contenu);
        contenu.getUtilisateursConsultants().remove(this);
        return this;
    }

    public void setContenusConsultes(Set<Contenu> contenus) {
        this.contenusConsultes = contenus;
    }

    public Set<Prestation> getServicesRecuses() {
        return servicesRecuses;
    }

    public Utilisateur servicesRecuses(Set<Prestation> prestations) {
        this.servicesRecuses = prestations;
        return this;
    }

    public Utilisateur addServicesRecus(Prestation prestation) {
        servicesRecuses.add(prestation);
        prestation.getBeneficiaires().add(this);
        return this;
    }

    public Utilisateur removeServicesRecus(Prestation prestation) {
        servicesRecuses.remove(prestation);
        prestation.getBeneficiaires().remove(this);
        return this;
    }

    public void setServicesRecuses(Set<Prestation> prestations) {
        this.servicesRecuses = prestations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Utilisateur utilisateur = (Utilisateur) o;
        if (utilisateur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, utilisateur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + id +
            ", credits='" + credits + "'" +
            '}';
    }
}
