package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarMedicoComOutraConsultaNoMesmoHorario implements ValidadorAgendamentoDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosAgendamentoConsulta dados){

        //Não permitir o agendamento de uma consulta com um médico que já possui outra consulta agendada na mesma data/hora;

        var medicoComOutraConsultaNoMesmoHorario = repository.existsByMedicoIdAndData(dados.idMedico(), dados.data());

        if(medicoComOutraConsultaNoMesmoHorario){
            throw new ValidacaoException("Médico já possui consulta agendada nesse horário!");
        }
    }
}
