package io.github.edevaldojr.msavaliadorcredito.infra.mqueue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.edevaldojr.msavaliadorcredito.application.model.DadosSolicitacaoEmissaoCartao;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SolicitacaoEmissaoCartaoPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    private final Queue queueEmissaoCartaos;

    public void solicitarCartao(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        var json = this.convertIntoJson(dados);
        rabbitTemplate.convertAndSend(queueEmissaoCartaos.getName(), json);
    }

    private String convertIntoJson(DadosSolicitacaoEmissaoCartao dados) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(dados);
        return json;
    }
}
