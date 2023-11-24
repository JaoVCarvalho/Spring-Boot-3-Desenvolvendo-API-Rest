package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.domain.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidadorMedicoAtivo implements ValidadorAgendamentoDeConsultas{

    @Autowired
    private MedicoRepository repository;

    public void validar(DadosAgendamentoConsulta dados){

        // Não permitir o agendamento de consultas com médicos inativos no sistema;

        // Trata o fato do médico ser opcional
        if(dados.idMedico() == null){
            return;
        }

        var medicoEstaAtivo = repository.findAtivoById(dados.idMedico());

        if(!medicoEstaAtivo){
            throw new ValidacaoException("Consulta não pode ser realizada por esse médico!");
        }

    }
}
