package io.github.edevaldojr.msavaliadorcredito.application.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.edevaldojr.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.ErroComunicacaoMicrosserviceException;
import io.github.edevaldojr.msavaliadorcredito.application.model.DadosAvaliacao;
import io.github.edevaldojr.msavaliadorcredito.application.model.RetornoAvaliacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.SituacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.service.AvaliadorCreditService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {
    
    private final AvaliadorCreditService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "ok";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity<SituacaoCliente> consultaSituacaoCliente(@RequestParam("cpf") String cpf) {
        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);

            return ResponseEntity.ok(situacaoCliente);   
        } catch (DadosClienteNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicrosserviceException exception) {
            return ResponseEntity.status(HttpStatus.resolve(exception.getStatus())).build();
        }
    }

    @PostMapping
    public ResponseEntity<RetornoAvaliacaoCliente> realizarAvaliacao(@RequestBody DadosAvaliacao dados) {
        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());

            return ResponseEntity.ok(retornoAvaliacaoCliente);   
        } catch (DadosClienteNotFoundException exception) {
            return ResponseEntity.notFound().build();
        } catch (ErroComunicacaoMicrosserviceException exception) {
            return ResponseEntity.status(HttpStatus.resolve(exception.getStatus())).build();
        }
    }
}
