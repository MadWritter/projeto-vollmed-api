package com.vollmed.api.model.component;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.vollmed.api.model.service.ConsultaService;

/**
 * Componente que controla a data e hora do sistema,
 * fornece as informações de data e hora atual, também é útil
 * para os testes unitários
 *
 * @since branch consultas
 * @author Jean Maciel
 * @see ConsultaService
 */
@Component
public class ClockConfig {

    private Clock clock = Clock.systemDefaultZone();

    /**
    * Retorna a data e hora atual do sistema, baseado
    * no relógio informado
    *
    * @return
    */
    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    /**
    * Fixa um relógio para o componente, para uso nos testes
    * unitários
    *
    * @param clock
    */
    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
