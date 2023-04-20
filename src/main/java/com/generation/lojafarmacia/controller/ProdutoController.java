package com.generation.lojafarmacia.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.lojafarmacia.model.Produto;
import com.generation.lojafarmacia.repository.CategoriaRepository;
import com.generation.lojafarmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	// Injeção de dependências
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	// Métodos CRUD
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
		return categoriaRepository.findById(produto.getCategoria().getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto)))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
	}
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto){
		return produtoRepository.findById(produto.getId())
				.map(respostaId -> categoriaRepository.findById(produto.getCategoria().getId())
						.map(respostaCategoria -> ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto)))
						.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> optional = produtoRepository.findById(id);
		
		if (optional.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		else {
			produtoRepository.deleteById(id);
		}
	}
	
	// Métodos Adicionais
	@GetMapping("{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}
	
	@GetMapping("/nome_ou_fabricante/{nome}/{fabricante}")
	public ResponseEntity<List<Produto>> getByNomeOrFabricante(@PathVariable String nome, @PathVariable String fabricante){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCaseOrFabricanteContainingIgnoreCase(nome, fabricante));
	}
	
	@GetMapping("/nome_e_fabricante/{nome}/{fabricante}")
	public ResponseEntity<List<Produto>> getByNomeAndFabricante(@PathVariable String nome, @PathVariable String fabricante){
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCaseAndFabricanteContainingIgnoreCase(nome, fabricante));
	}
	
	@GetMapping("/preco_entre/{inicio}/{fim}")
	public ResponseEntity<List<Produto>> getByPrecoBetween(@PathVariable BigDecimal inicio, @PathVariable BigDecimal fim){
		return ResponseEntity.ok(produtoRepository.findAllByPrecoBetween(inicio, fim));
	}
}
