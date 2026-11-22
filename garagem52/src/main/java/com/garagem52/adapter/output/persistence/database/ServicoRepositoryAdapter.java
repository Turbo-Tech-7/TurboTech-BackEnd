package com.garagem52.adapter.output.persistence.database;

import com.garagem52.adapter.output.persistence.mapper.ServicoMapper;
import com.garagem52.adapter.output.persistence.repository.MongoServicoRepository;
import com.garagem52.config.CacheConfig;
import com.garagem52.domain.model.Servico;
import com.garagem52.ports.output.ServicoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServicoRepositoryAdapter implements ServicoOutputPort {

    private final MongoServicoRepository repository;
    private final ServicoMapper mapper;

    // Toda escrita em Servico passa por aqui (ServicoService e OrcamentoService),
    // então é este o lugar de invalidar o cache da listagem.
    @Override
    @CacheEvict(cacheNames = CacheConfig.SERVICOS, allEntries = true)
    public Servico save(Servico servico) {
        return mapper.toDomain(repository.save(mapper.toEntity(servico)));
    }

    @Override
    public Optional<Servico> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    // Chave = página:tamanho:ordenação (#p0 = Pageable). Expira em 15 min (CacheConfig)
    // e é limpo a cada save/delete, então a tela do mecânico nunca fica defasada.
    @Override
    @Cacheable(cacheNames = CacheConfig.SERVICOS,
            key = "#p0.paged ? #p0.pageNumber + ':' + #p0.pageSize + ':' + #p0.sort : 'unpaged'")
    public Page<Servico> findAll(Pageable page) {
        return repository.findAll(page).map(mapper::toDomain);
    }

    @Override
    public List<Servico> findByVeiculoId(String veiculoId) {
        return repository.findByVeiculoId(veiculoId).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Servico> findByStatus(String status) {
        return repository.findByStatus(status).stream()
                .map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = CacheConfig.SERVICOS, allEntries = true)
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
