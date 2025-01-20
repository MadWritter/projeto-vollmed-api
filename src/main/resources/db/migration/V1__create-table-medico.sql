CREATE TABLE IF NOT EXISTS medico (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(11) NOT NULL UNIQUE,
    crm VARCHAR(6) NOT NULL UNIQUE,
    especialidade VARCHAR(20) NOT NULL CHECK (
        especialidade IN (
            'ORTOPEDIA',
            'CARDIOLOGIA',
            'GINECOLOGIA',
            'DERMATOLOGIA'
        )
    ),
    logradouro VARCHAR(100) NOT NULL,
    numero INT CHECK (numero BETWEEN 1 AND 99999),
    complemento VARCHAR(100),
    bairro VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    uf CHAR(2) NOT NULL CHECK (
        uf IN (
            'AC',
            'AL',
            'AP',
            'AM',
            'BA',
            'CE',
            'DF',
            'ES',
            'GO',
            'MA',
            'MT',
            'MS',
            'MG',
            'PA',
            'PB',
            'PR',
            'PE',
            'PI',
            'RJ',
            'RN',
            'RS',
            'RO',
            'RR',
            'SC',
            'SP',
            'SE',
            'TO'
        )
    ),
    cep VARCHAR(8) NOT NULL,
    PRIMARY KEY (id)
);
