package io.github.edevaldojr.mscartoes.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.edevaldojr.mscartoes.domain.ClienteCartao;
import io.github.edevaldojr.mscartoes.infra.repository.ClienteCartaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteCartaoService {
    
    private final ClienteCartaoRepository repository;

    public List<ClienteCartao> listarCartoesByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
