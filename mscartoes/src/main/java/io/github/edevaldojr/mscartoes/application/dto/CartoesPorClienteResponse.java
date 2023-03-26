package io.github.edevaldojr.mscartoes.application.dto;

import java.math.BigDecimal;

import io.github.edevaldojr.mscartoes.domain.ClienteCartao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartoesPorClienteResponse {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;

    public static CartoesPorClienteResponse fromModel(ClienteCartao model) {
        return new CartoesPorClienteResponse(
                    model.getCartao().getNome(),
                    model.getCartao().getBandeira().toString(),
                    model.getLimite()
        );
    }

}
