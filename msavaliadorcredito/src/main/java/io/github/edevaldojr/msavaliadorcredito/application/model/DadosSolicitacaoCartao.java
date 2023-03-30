package io.github.edevaldojr.msavaliadorcredito.application.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DadosSolicitacaoCartao {
    
    private Long idCartao;
    private String cpf;
    private String endereco;
    private BigDecimal limiteLiberado;
    
}
