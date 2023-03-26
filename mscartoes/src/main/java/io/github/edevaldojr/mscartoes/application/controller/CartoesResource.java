package io.github.edevaldojr.mscartoes.application.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.edevaldojr.mscartoes.application.dto.CartaoSaveRequest;
import io.github.edevaldojr.mscartoes.application.dto.CartoesPorClienteResponse;
import io.github.edevaldojr.mscartoes.application.service.CartaoService;
import io.github.edevaldojr.mscartoes.application.service.ClienteCartaoService;
import io.github.edevaldojr.mscartoes.domain.Cartao;
import io.github.edevaldojr.mscartoes.domain.ClienteCartao;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("cartoes")
@RequiredArgsConstructor
public class CartoesResource {
    
    private final CartaoService cartaoService;

    private final ClienteCartaoService clienteCartaoService;


    @GetMapping
    public String status(){
        return "ok";
    }

    @PostMapping
    public ResponseEntity<Void> cadastra(@RequestBody CartaoSaveRequest request) {
        Cartao cartao = request.toModel();
        cartaoService.save(cartao);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(params = "renda")
    public ResponseEntity<List<Cartao>> getCartoesRendaAte(@RequestParam("renda") Long renda) {
        List<Cartao> cartoes = cartaoService.getCartoesRendaMenorIgual(renda);
        return ResponseEntity.ok(cartoes);
    }

    @GetMapping(params = "cpf")
    public ResponseEntity<List<CartoesPorClienteResponse>> getCartoesByCliente(@RequestParam("cpf") String cpf) {
        List<ClienteCartao> clientesCartoes = clienteCartaoService.listarCartoesByCpf(cpf);
        List<CartoesPorClienteResponse> cartoesPorCliente = clientesCartoes.stream()
                                                        .map(CartoesPorClienteResponse::fromModel)
                                                        .collect(Collectors.toList());
        return ResponseEntity.ok(cartoesPorCliente);
    }
}
