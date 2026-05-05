package com.lulafreire.apsirece.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MensagemDTO {
    private Long id;
    private String conteudo;
    private LocalDateTime dataEnvio;
    private String remetenteUsername;
    private Long grupoId;
    private Long destinatarioId;
}