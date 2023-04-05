package io.github.edevaldojr.msavaliadorcredito.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.DadosClienteNotFoundException;
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.ErroComunicacaoMicrosserviceException;
import io.github.edevaldojr.msavaliadorcredito.application.exceptions.ErroSolicitacaoCartaoException;
import io.github.edevaldojr.msavaliadorcredito.application.model.Cartao;
import io.github.edevaldojr.msavaliadorcredito.application.model.CartaoAprovado;
import io.github.edevaldojr.msavaliadorcredito.application.model.CartaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.DadosCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.DadosSolicitacaoEmissaoCartao;
import io.github.edevaldojr.msavaliadorcredito.application.model.ProtocoloSolicitacaoCartao;
import io.github.edevaldojr.msavaliadorcredito.application.model.RetornoAvaliacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.application.model.SituacaoCliente;
import io.github.edevaldojr.msavaliadorcredito.infra.clients.CartoesResourceClient;
import io.github.edevaldojr.msavaliadorcredito.infra.clients.ClienteResourceClient;
import io.github.edevaldojr.msavaliadorcredito.infra.mqueue.SolicitacaoEmissaoCartaoPublisher;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliadorCreditoService {

    private final ClienteResourceClient clientesClient;
    private final CartoesResourceClient cartoesClient;
    private final SolicitacaoEmissaoCartaoPublisher emissaoCartaoPublisher;

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

    public  RetornoAvaliacaoCliente realizarAvaliacao(String cpf, Long renda) throws DadosClienteNotFoundException, ErroComunicacaoMicrosserviceException {
        try {
            ResponseEntity<DadosCliente> dadosClienteReponse = clientesClient.dadosCliente(cpf);
            ResponseEntity<List<Cartao>> cartoesResponse = cartoesClient.getCartoesRendaAte(renda);

            List<Cartao> cartoes = cartoesResponse.getBody();
            var listaCartoesAprovados = cartoes.stream().map(cartao -> {
                DadosCliente dadosCliente = dadosClienteReponse.getBody();

                BigDecimal limiteBasico = cartao.getLimiteBasico();
                BigDecimal idadeBD = BigDecimal.valueOf(dadosCliente.getIdade());
                var fator = idadeBD.divide(BigDecimal.valueOf(10));
                BigDecimal limiteAprovado = fator.multiply(limiteBasico);

                CartaoAprovado aprovado = new CartaoAprovado();
                aprovado.setCartao(cartao.getNome());
                aprovado.setBandeira(cartao.getBandeira());
                aprovado.setLimiteAprovado(limiteAprovado);

                return aprovado;
            }).collect(Collectors.toList());

            return new RetornoAvaliacaoCliente(listaCartoesAprovados);

        } catch(FeignException.FeignClientException exception) {
            int status = exception.status();
            if(HttpStatus.NOT_FOUND.value() == status) {
                throw new DadosClienteNotFoundException();
            }
            throw new ErroComunicacaoMicrosserviceException(exception.getMessage(), status);
        }
    }

    public ProtocoloSolicitacaoCartao solicitarEmissaoCartao(DadosSolicitacaoEmissaoCartao dados) {
        try {
            this.emissaoCartaoPublisher.solicitarCartao(dados);
            var protocolo = UUID.randomUUID().toString();
            return new ProtocoloSolicitacaoCartao(protocolo);
        } catch (Exception exception) {
            throw new ErroSolicitacaoCartaoException(exception.getMessage());                           
        }
    }
    
}
