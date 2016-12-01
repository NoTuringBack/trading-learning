package com.noturingback.tradinglearning.repository;

import com.noturingback.tradinglearning.domain.Contenu;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contenu entity.
 */
@SuppressWarnings("unused")
public interface ContenuRepository extends JpaRepository<Contenu,Long> {

}
