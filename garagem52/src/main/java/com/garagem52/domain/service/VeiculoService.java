package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.UpdateVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.adapter.output.persistence.mapper.VeiculoPersistenceMapper;
import com.garagem52.domain.exception.veiculo.VeiculoNotFoundException;
import com.garagem52.domain.model.Veiculo;
import com.garagem52.ports.input.VeiculoInputPort;
import com.garagem52.ports.output.VeiculoOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VeiculoService implements VeiculoInputPort {

    private final VeiculoOutputPort veiculoOutputPort;
    private final VeiculoPersistenceMapper mapper;
    private final WebClient webClient;

    @Value("${api.token}")
    private String token;

    @Override
    public VeiculoResponseDTO criarVeiculo(String placa) {
        placa = placa.trim().toUpperCase();
        if (!placaValida(placa)) throw new RuntimeException("Formato de placa inválido");

        Optional<Veiculo> cache = veiculoOutputPort.findByPlaca(placa);
        if (cache.isPresent()) return mapper.toResponseDTO(cache.get());

        Veiculo veiculoApi = webClient.get()
                .uri("https://wdapi2.com.br/consulta/{placa}/{token}", placa, token)
                .retrieve().bodyToMono(Veiculo.class).block();

        if (veiculoApi == null) throw new RuntimeException("Veículo não encontrado na API");
        veiculoApi.setPlaca(placa);

        return mapper.toResponseDTO(veiculoOutputPort.save(veiculoApi));
    }

    @Override
    public VeiculoResponseDTO findById(String id) {
        return mapper.toResponseDTO(veiculoOutputPort.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id)));
    }

    @Override
    public List<VeiculoResponseDTO> findAll() {
        return veiculoOutputPort.findAll().stream().map(mapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public VeiculoResponseDTO findByPlaca(String placa) {
        return mapper.toResponseDTO(veiculoOutputPort.findByPlaca(placa)
                .orElseThrow(() -> new VeiculoNotFoundException(placa)));
    }

    @Override
    public VeiculoResponseDTO updateVeiculo(String id, UpdateVeiculoRequestDTO requestDTO) {
        Veiculo v = veiculoOutputPort.findById(id).orElseThrow(() -> new VeiculoNotFoundException(id));
        if (requestDTO.getMarca() != null)  v.setMarca(requestDTO.getMarca());
        if (requestDTO.getModelo() != null) v.setModelo(requestDTO.getModelo());
        if (requestDTO.getCor() != null)    v.setCor(requestDTO.getCor());
        return mapper.toResponseDTO(veiculoOutputPort.save(v));
    }

    @Override
    public void delete(String id) {
        veiculoOutputPort.findById(id).orElseThrow(() -> new VeiculoNotFoundException(id));
        veiculoOutputPort.deleteById(id);
    }

    private boolean placaValida(String placa) {
        return placa.matches("^[A-Z]{3}[0-9]{4}$") || placa.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
    }
}
