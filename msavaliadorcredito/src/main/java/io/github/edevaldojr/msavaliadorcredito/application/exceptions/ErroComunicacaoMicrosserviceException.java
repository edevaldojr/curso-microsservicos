package io.github.edevaldojr.msavaliadorcredito.application.exceptions;

import lombok.Getter;

public class ErroComunicacaoMicrosserviceException extends Exception{
    
    @Getter
    private Integer status;

    public ErroComunicacaoMicrosserviceException(String msg, Integer status) {
        super(msg);
        this.status = status;
    }
}
