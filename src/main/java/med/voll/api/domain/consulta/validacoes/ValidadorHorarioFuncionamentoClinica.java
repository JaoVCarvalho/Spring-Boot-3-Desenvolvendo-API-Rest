package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;

@Component
public class ValidadorHorarioFuncionamentoClinica implements ValidadorAgendamentoDeConsultas{

    public void validar(DadosAgendamentoConsulta dados){

        //O horário de funcionamento da clínica é de segunda a sábado, das 07:00 às 19:00;

        var domingo = dados.data().getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesHorario = dados.data().getHour() < 7;
        var depoisHorario = dados.data().getHour() > 18;

        if(domingo || antesHorario || depoisHorario){
            throw new ValidacaoException("Consulta fora do horário de atendimento!");
        }
    }
}
