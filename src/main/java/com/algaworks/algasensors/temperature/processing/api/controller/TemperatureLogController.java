package com.algaworks.algasensors.temperature.processing.api.controller;

import com.algaworks.algasensors.temperature.processing.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature.processing.common.IdGenerator;
import com.algaworks.algasensors.temperature.processing.infrastructure.rabbitmq.RabbitMQConfig;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
@Slf4j
@RequiredArgsConstructor
public class TemperatureLogController {

    private final RabbitTemplate rabbitTemplate;


    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable TSID sensorId, @RequestBody String input) {
        if (input == null || input.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        double temperature;
        try {
            temperature = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var logTemp = TemperatureLogOutput.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .sensorId(sensorId)
                .value(temperature)
                .registeredAt(OffsetDateTime.now())
                .build();
        log.info(logTemp.toString());

        String exchange = RabbitMQConfig.TEMPERATURE_PROCESSING_TEMPERATURE_RECEIVED_V_1_E;
        String routingKey = "";

        //Exemplo com String
        //String payload = logTemp.toString();

        //Exemplo com Object - envia objeto direto após config da serialização
        // rabbitTemplate.convertAndSend(exchange, routingKey, logTemp);

        //Exemplo com cabeçalho
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setHeader("sensorId", logTemp.getSensorId().toString());
            return message;
        };
        rabbitTemplate.convertAndSend(exchange, routingKey, logTemp, messagePostProcessor);
    }


}
