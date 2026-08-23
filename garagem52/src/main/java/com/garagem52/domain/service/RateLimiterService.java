package com.garagem52.domain.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private static final int MAX_TENTATIVAS = 5;
    private static final int MINUTOS_BLOQUEIO = 15;

    private final Map<String, List<LocalDateTime>> tentativas = new ConcurrentHashMap<>();

    public synchronized boolean podeTentar(String chave) {

        LocalDateTime agora = LocalDateTime.now();

        List<LocalDateTime> lista = tentativas.computeIfAbsent(chave, k -> new ArrayList<>()
        );


        lista.removeIf(data -> data.isBefore(agora.minusMinutes(MINUTOS_BLOQUEIO))
        );

        if (lista.size() >= MAX_TENTATIVAS) {
            return false;
        }

        lista.add(agora);

        return true;
    }

    public void resetar(String chave) {
        tentativas.remove(chave);
    }
}