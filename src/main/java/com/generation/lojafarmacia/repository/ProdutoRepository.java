package com.generation.lojafarmacia.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.generation.lojafarmacia.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	List<Produto> findAllByNomeContainingIgnoreCase(@Param("nome") String nome);
	List<Produto> findAllByNomeContainingIgnoreCaseOrFabricanteContainingIgnoreCase(@Param("nome") String nome, @Param("fabricante") String fabricante);
	List<Produto> findAllByNomeContainingIgnoreCaseAndFabricanteContainingIgnoreCase(@Param("nome") String nome, @Param("fabricante") String fabricante);
	List<Produto> findAllByPrecoBetween(@Param("inicio") BigDecimal inicio, @Param("fim") BigDecimal fim);
}
