package com.garagem52.domain.service;

import com.garagem52.adapter.input.dto.request.CreateClienteVeiculoRequestDTO;
import com.garagem52.adapter.input.dto.response.ClienteVeiculoResponseDTO;
import com.garagem52.adapter.input.dto.response.VeiculoResponseDTO;
import com.garagem52.domain.model.ClienteVeiculo;
import com.garagem52.ports.input.ClienteVeiculoInputPort;
import com.garagem52.ports.input.VeiculoInputPort;
import com.garagem52.ports.output.ClienteVeiculoOutputPort;
import com.garagem52.ports.output.VeiculoOutputPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ClienteVeiculoService implements ClienteVeiculoInputPort {

    private final ClienteVeiculoOutputPort outputPort;
    private final VeiculoOutputPort veiculoOutputPort;
    private final VeiculoInputPort veiculoInputPort;

    @Override
    public ClienteVeiculoResponseDTO cadastrar(CreateClienteVeiculoRequestDTO request) {
        String placa = request.getPlacaVeiculo().trim().toUpperCase();

        // Sempre usa criarVeiculo — verifica se já existe no banco antes de ir na API.
        // Garante que o veículo estará persistido e com ID antes de salvar o ClienteVeiculo.
        VeiculoResponseDTO veiculoDTO = veiculoInputPort.criarVeiculo(placa);

        ClienteVeiculo cv = ClienteVeiculo.builder()
                .nomeCliente(request.getNomeCliente())
                .telefoneCliente(request.getTelefoneCliente())
                .emailCliente(request.getEmailCliente())
                .placaVeiculo(placa)
                .modeloVeiculo(veiculoDTO.getModelo() != null
                        ? veiculoDTO.getMarca() + " " + veiculoDTO.getModelo()
                        : request.getModeloVeiculo())
                .veiculoId(veiculoDTO.getId())
                .dataInclusao(LocalDateTime.now())
                .build();

        return toResponse(outputPort.save(cv), veiculoDTO);
    }

    @Override
    public ClienteVeiculoResponseDTO findById(String id) {
        ClienteVeiculo cv = outputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente/Veículo não encontrado: " + id));
        return toResponse(cv, resolverVeiculo(cv.getVeiculoId()));
    }

    @Override
    public List<ClienteVeiculoResponseDTO> findAll() {
        return outputPort.findAll().stream()
                .map(cv -> toResponse(cv, resolverVeiculo(cv.getVeiculoId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteVeiculoResponseDTO> findByNome(String nome) {
        return outputPort.findByNomeCliente(nome).stream()
                .map(cv -> toResponse(cv, resolverVeiculo(cv.getVeiculoId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteVeiculoResponseDTO> findByPlaca(String placa) {
        return outputPort.findByPlaca(placa).stream()
                .map(cv -> toResponse(cv, resolverVeiculo(cv.getVeiculoId())))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        outputPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente/Veículo não encontrado: " + id));
        outputPort.deleteById(id);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private VeiculoResponseDTO resolverVeiculo(String veiculoId) {
        if (veiculoId == null) return null;
        return veiculoOutputPort.findById(veiculoId)
                .map(v -> VeiculoResponseDTO.builder()
                        .id(v.getId()).marca(v.getMarca()).modelo(v.getModelo())
                        .ano(v.getAno()).placa(v.getPlaca()).cor(v.getCor())
                        .build())
                .orElse(null);
    }

    private ClienteVeiculoResponseDTO toResponse(ClienteVeiculo cv, VeiculoResponseDTO veiculo) {
        return ClienteVeiculoResponseDTO.builder()
                .id(cv.getId())
                .nomeCliente(cv.getNomeCliente())
                .telefoneCliente(cv.getTelefoneCliente())
                .emailCliente(cv.getEmailCliente())
                .placaVeiculo(cv.getPlacaVeiculo())
                .modeloVeiculo(cv.getModeloVeiculo())
                .veiculoId(cv.getVeiculoId())
                .veiculo(veiculo)
                .build();
    }
}
