package io.github.edevaldojr.msavaliadorcredito.application.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.ErroComunicacaoMicrosserviceException;
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

    public SituacaoCliente obterSituacaoCliente(String cpf) throws DadosClienteNotFoundException, ErroComunicacaoMicrosserviceException {
        try {
            ResponseEntity<DadosCliente> dadosClienteReponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<CartaoCliente>> dadosCartoesReponse = cartoesClient.getCartoesByCliente(cpf);

            return SituacaoCliente
                        .builder()
                        .cliente(dadosClienteReponse.getBody())
                        .cartoes(dadosCartoesReponse.getBody())
                        .build();
        } catch(FeignException.FeignClientException exception) {
            int status = exception.status();
            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosserviceException(exception.getMessage(), status);
        }
    }
    
}
