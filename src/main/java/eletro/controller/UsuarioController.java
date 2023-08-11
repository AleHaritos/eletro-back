package eletro.controller;

import eletro.domain.Usuario;
import eletro.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.salvarUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> getByEmailAndSenha(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.getByEmailAndSenha(usuario));
    }

    @PostMapping("/admin")
    public ResponseEntity<Boolean> getAdm(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.getAdmByEmail(usuario.getEmail()));
    }
}
