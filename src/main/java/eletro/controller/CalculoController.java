package eletro.controller;

import eletro.domain.dto.Cep;
import eletro.domain.dto.DadosFrete;
import eletro.service.CalculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/calculo")
public class CalculoController {

    @Autowired
    CalculoService calculoService;

    @GetMapping("/frete")
    public ResponseEntity<DadosFrete> calcularFrete(@RequestParam String cep) {
        //Validar CEP
        try {
            return ResponseEntity.ok(calculoService.calcularFrete(cep));
        } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cep")
    public ResponseEntity<Cep> consultarCep(@RequestParam String cep) {
        try {
            return ResponseEntity.ok(calculoService.calcularCEP(cep));
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
