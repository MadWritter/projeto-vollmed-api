CREATE TABLE IF NOT EXISTS consulta (
    id BIGINT NOT NULL AUTO_INCREMENT,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_da_consulta DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL CHECK(
        status IN ('AGENDADA', 'CONCLUIDA', 'CANCELADA')),
    FOREIGN KEY(paciente_id) REFERENCES paciente(id),
    FOREIGN KEY(medico_id) REFERENCES medico(id),
    PRIMARY KEY(id)
);