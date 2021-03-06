package com.osedit.spring.repository

import com.osedit.spring.domain.NpcDefinition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface NpcRepository : JpaRepository<NpcDefinition, Int> {
    @Query("SELECT def FROM NpcDefinition def WHERE name IN (:searchText) OR " +
            ":searchText IN ELEMENTS(actions) ")
    fun searchNpcs(@Param("searchText") searchText: String, pageable: Pageable): Page<NpcDefinition>
}