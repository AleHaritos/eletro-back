package eletro.controller;

import eletro.domain.Imagem;
import eletro.repository.ImagemRepository;
import eletro.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/imagem")
public class ImagemController {

    @Autowired
    ImagemRepository imagemRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> insertImagem(@PathVariable Integer id, @RequestBody String imagem) {
        Imagem image = new Imagem(imagem, produtoRepository.findById(id).get());
        this.imagemRepository.save(image);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Imagem> getImagem(@PathVariable Integer id) {
        List<Imagem> i = imagemRepository.findByProduto(produtoRepository.findById(id).get());
        if (i.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(i.get(0));
        }
    }
}
