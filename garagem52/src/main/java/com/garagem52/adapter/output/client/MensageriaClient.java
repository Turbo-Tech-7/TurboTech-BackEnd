package com.garagem52.adapter.output.client;

import com.garagem52.adapter.output.client.dto.PublicarEventoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mensageria-service", url = "${app.mensageria.url}")
public interface MensageriaClient {

    @PostMapping("/eventos")
    ResponseEntity<Void> publicar(@RequestBody PublicarEventoRequestDTO evento);
}
