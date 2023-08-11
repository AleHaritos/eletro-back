package eletro.controller;

import eletro.domain.Produto;
import eletro.service.CalculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import eletro.repository.ProdutoRepository;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/produto")
public class ProdutoController {

    @Autowired
    ProdutoRepository repository;

    @PostMapping
    public ResponseEntity<Produto> insertProduto(@RequestBody Produto produto) {
        return ResponseEntity.ok(repository.save(produto));
    }

    @GetMapping
    public ResponseEntity<List<Produto>> getProdutos() {
        return ResponseEntity.ok(repository.findAllByOrderById());
    }

    @GetMapping("/oferta")
    public ResponseEntity<List<Produto>> getProdutosComDesconto() {
        return ResponseEntity.ok(repository.findFirst20ByOffGreaterThanAndPrecoGreaterThanAndEstoqueGreaterThanOrderByIdDesc(0,0.0, 0));
    }

    @GetMapping("/semOferta")
    public ResponseEntity<List<Produto>> getProdutosSemDesconto() {
        return ResponseEntity.ok(repository.findFirst20ByOffLessThanEqualOrOffIsNullAndPrecoIsNullOrPrecoLessThanEqualAndEstoqueGreaterThanOrderByIdDesc(0,0.0, 0));
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Produto>> getProdutosByCategoria(@PathVariable String categoria, @RequestParam Integer page) {
        return ResponseEntity.ok(repository.findByCategoriasPageable(categoria, 10, page * 10).get());
    }

    @GetMapping("/count/{categoria}")
    public ResponseEntity<Integer> getCountCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(repository.countProdutosByCategoria(categoria));
    }

    @DeleteMapping
    public ResponseEntity<Void> excluirProduto(@RequestParam Integer id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll/{id}")
    public ResponseEntity<Produto> getProdutosByIdWithImage(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(repository.findProdutoWithImage(id).get());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutosById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(repository.findById(id).get());
    }

}
