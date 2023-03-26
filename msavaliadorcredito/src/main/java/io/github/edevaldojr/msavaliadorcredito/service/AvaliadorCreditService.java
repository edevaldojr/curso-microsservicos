package io.github.edevaldojr.msavaliadorcredito.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.edevaldojr.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.edevaldojr.msavaliadorcredito.model.DadosCliente;
import io.github.edevaldojr.msavaliadorcredito.model.SituacaoCliente;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditService {

    private final ClienteResourceClient clientesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {

        ResponseEntity<DadosCliente> dadosClienteReponse = clientesClient.dadosCliente(cpf);

        return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteReponse.getBody())
                    .build();
    }
    
}
