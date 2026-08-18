package com.clinina.sistema.repository;

import com.clinina.sistema.model.entity.Grupo;
import com.clinina.sistema.model.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface ProdutosServicosRepository extends JpaRepository<Produto, Long> {
    boolean existsByGrupo(Grupo grupo);

    boolean existsByCodigoDeBarras(String codigoDeBarras);

    @Query(value = """
    SELECT *
    FROM tb_produtos
    WHERE MATCH(nome_normalizado)
    AGAINST(:termo IN NATURAL LANGUAGE MODE)
    ORDER BY MATCH(nome_normalizado)
    AGAINST(:termo IN NATURAL LANGUAGE MODE) DESC
    """, nativeQuery = true)
    List<Produto> buscarPorNome(@Param("termo") String termo);

    List<Produto> findTop20ByNomeNormalizadoContaining(String termo);

    Optional<Produto> findByCodigoDeBarras(String termo);

    Page<Produto> findAll(Pageable pageable);

    @Query(value = """
    SELECT *
    FROM tb_produtos
    WHERE MATCH(nome_normalizado)
    AGAINST(:termo IN NATURAL LANGUAGE MODE)
    ORDER BY MATCH(nome_normalizado)
    AGAINST(:termo IN NATURAL LANGUAGE MODE) DESC
    """,
            countQuery = """
    SELECT COUNT(*)
    FROM tb_produtos
    WHERE MATCH(nome_normalizado)
    AGAINST(:termo IN NATURAL LANGUAGE MODE)
    """,
            nativeQuery = true)
    Page<Produto> buscarPorNome(
            @Param("termo") String termo,
            Pageable pageable
    );

    List<Produto> findByDataValidadeBefore(LocalDate data);
    List<Produto> findByDataValidadeBetween(LocalDate inicio, LocalDate fim);
}
