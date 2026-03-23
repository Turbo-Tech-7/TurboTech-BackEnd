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

        if (!placaValida(placa)) {
            throw new RuntimeException("Formato de placa inválido");
        }

        Optional<Veiculo> cache = veiculoOutputPort.findByPlaca(placa);

        if (cache.isPresent()) {
            System.out.println("Veículo encontrado no banco (cache)");

            return mapper.toResponseDTO(cache.get());
        }

        System.out.println("Consultando API externa...");

        Veiculo veiculoApi = webClient.get()
                .uri("https://wdapi2.com.br/consulta/{placa}/{token}", placa, token)
                .retrieve()
                .bodyToMono(Veiculo.class)
                .block();

        if (veiculoApi == null) {
            throw new RuntimeException("Veículo não encontrado na API");
        }

        veiculoApi.setPlaca(placa);

        Veiculo salvo = veiculoOutputPort.save(veiculoApi);

        return mapper.toResponseDTO(salvo);
    }

    @Override
    public VeiculoResponseDTO findById(Long id) {
        Veiculo veiculo = veiculoOutputPort.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id));
        return mapper.toResponseDTO(veiculo);
    }

    @Override
    public List<VeiculoResponseDTO> findAll() {
        return veiculoOutputPort.findAll()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public VeiculoResponseDTO findByPlaca(String placa) {
        Veiculo veiculo = veiculoOutputPort.findByPlaca(placa)
                .orElseThrow(() -> new VeiculoNotFoundException(placa));
        return mapper.toResponseDTO(veiculo);
    }

    @Override
    public VeiculoResponseDTO updateVeiculo(Long id, UpdateVeiculoRequestDTO requestDTO) {

        Veiculo veiculoExistente = veiculoOutputPort.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id));

        if (requestDTO.getMarca() != null) {
            veiculoExistente.setMarca(requestDTO.getMarca());
        }

        if (requestDTO.getModelo() != null) {
            veiculoExistente.setModelo(requestDTO.getModelo());
        }

        if (requestDTO.getCor() != null) {
            veiculoExistente.setCor(requestDTO.getCor());
        }

        Veiculo veiculoAtualizado = veiculoOutputPort.save(veiculoExistente);

        return mapper.toResponseDTO(veiculoAtualizado);
    }

    @Override
    public void delete(Long id) {
        veiculoOutputPort.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id));
        veiculoOutputPort.deleteById(id);
    }

    private VeiculoResponseDTO toResponseDTO(Veiculo veiculo){
        return VeiculoResponseDTO.builder()
                .id(veiculo.getId())
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .ano(veiculo.getAno())
                .placa(veiculo.getPlaca())
                .cor(veiculo.getCor())
                .build();
    }

    private boolean placaValida(String placa) {

        String regexAntiga = "^[A-Z]{3}[0-9]{4}$";
        String regexMercosul = "^[A-Z]{3}[0-9][A-Z][0-9]{2}$";

        return placa.matches(regexAntiga) || placa.matches(regexMercosul);
    }
}
