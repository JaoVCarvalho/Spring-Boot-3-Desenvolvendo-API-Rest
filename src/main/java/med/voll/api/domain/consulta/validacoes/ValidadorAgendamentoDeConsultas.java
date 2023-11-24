package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosAgendamentoConsulta;


// Interfaces são carregadas automaticamente pelo Spring, sem a necessidade de anotações
public interface ValidadorAgendamentoDeConsultas {
    void validar(DadosAgendamentoConsulta dados);
}
