package io.github.edevaldojr.msavaliadorcredito.application.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartaoCliente {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

}
