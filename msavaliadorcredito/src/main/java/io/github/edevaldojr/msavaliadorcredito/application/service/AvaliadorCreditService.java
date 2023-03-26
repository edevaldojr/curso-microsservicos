package io.github.edevaldojr.msavaliadorcredito.application.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import io.github.edevaldojr.msavaliadorcredito.application.model.CartaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.DadosCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.SituacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.edevaldojr.msavaliadorcredito.infra.clients.ClienteResourceClient;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;

    public SituacaoCliente obterSituacaoCliente(String cpf) {

        ResponseEntity<DadosCliente> dadosClienteReponse = clientesClient.dadosCliente(cpf);
        ResponseEntity<List<CartaoCliente>> dadosCartoesReponse = cartoesClient.getCartoesByCliente(cpf);

        return SituacaoCliente
                    .builder()
                    .cliente(dadosClienteReponse.getBody())
                    .cartoes(dadosCartoesReponse.getBody())
                    .build();
    }
    
}
