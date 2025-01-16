package com.vollmed.api.model.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vollmed.api.model.repository.MedicoRepository;

@ExtendWith(MockitoExtension.class)
public class MedicoServiceTest {

    @InjectMocks
    private MedicoService medicoService;
    @Mock
    private MedicoRepository medicoRepository;

    //TODO iniciar o desenvolvimento do serviço
}
