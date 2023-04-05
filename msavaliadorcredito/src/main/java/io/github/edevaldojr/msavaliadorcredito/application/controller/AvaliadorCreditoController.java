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
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.ErroSolicitacaoCartaoException;
import io.github.edevaldojr.msavaliadorcredito.application.model.DadosAvaliacao;
import io.github.edevaldojr.msavaliadorcredito.application.model.DadosSolicitacaoEmissaoCartao;
import io.github.edevaldojr.msavaliadorcredito.application.model.ProtocoloSolicitacaoCartao;
import io.github.edevaldojr.msavaliadorcredito.application.model.RetornoAvaliacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.SituacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.service.AvaliadorCreditoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("avaliacoes-credito")
@RequiredArgsConstructor
public class AvaliadorCreditoController {
    
    private final AvaliadorCreditoService avaliadorCreditoService;

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

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity<ProtocoloSolicitacaoCartao> solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        } catch (ErroSolicitacaoCartaoException exception) {
            return ResponseEntity.internalServerError().build();
        }

    }
}
