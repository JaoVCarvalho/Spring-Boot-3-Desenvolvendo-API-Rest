package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorHorarioAntecedencia implements ValidadorAgendamentoDeConsultas{

    public void validar(DadosAgendamentoConsulta dados){

        // As consultas devem ser agendadas com antecedência mínima de 30 minutos;

        var horarioConsulta = dados.data();
        var horarioAgora = LocalDateTime.now();
        var diferencaMinutos = Duration.between(horarioAgora, horarioConsulta).toMinutes();

        if(diferencaMinutos < 30){
            throw new ValidacaoException("Consulta deve ser realizada com antecedência mínima de 30 minutos!");
        }

    }
}
