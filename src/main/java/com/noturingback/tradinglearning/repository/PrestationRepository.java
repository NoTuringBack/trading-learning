package com.noturingback.tradinglearning.repository;

import com.noturingback.tradinglearning.domain.Prestation;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Prestation entity.
 */
@SuppressWarnings("unused")
public interface PrestationRepository extends JpaRepository<Prestation,Long> {

}
