package com.noturingback.tradinglearning.repository;

import com.noturingback.tradinglearning.domain.Utilisateur;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {

    @Query("select distinct utilisateur from Utilisateur utilisateur left join fetch utilisateur.contenusConsultes left join fetch utilisateur.servicesRecuses")
    List<Utilisateur> findAllWithEagerRelationships();

    @Query("select utilisateur from Utilisateur utilisateur left join fetch utilisateur.contenusConsultes left join fetch utilisateur.servicesRecuses where utilisateur.id =:id")
    Utilisateur findOneWithEagerRelationships(@Param("id") Long id);

    Utilisateur findByUser_Login(String userLogin);
}
