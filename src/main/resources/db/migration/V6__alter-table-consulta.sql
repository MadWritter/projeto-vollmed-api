ALTER TABLE consulta ADD COLUMN motivo_cancelamento VARCHAR(20) CHECK(motivo_cancelamento IN ('PACIENTE_DESISTIU', 'MEDICO_CANCELOU', 'OUTROS'));
ALTER TABLE consulta ADD COLUMN observacao_cancelamento VARCHAR(100);
