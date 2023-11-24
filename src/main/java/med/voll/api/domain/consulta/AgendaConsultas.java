package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validacoes.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// A Classe de Serviço executa as regras de negócio e validações da aplicação
@Service
public class AgendaConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Spring detecta que o generics é uma interface e automaticamente busca todas as classes implementadas por ela
    @Autowired
    List<ValidadorAgendamentoDeConsultas> validadores;


    public void agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("ID do paciente não existe");
        }

        if(dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("ID do médico não existe!");
        }

        // Dessa forma, nossos dados percorrem por todas as validações de maneira muito flexível
        // Ou seja, caso queiramos adicionar um novo validador, basta implementá-lo implementando a interface
        // Ademais, para deletar um validador basta deletá-lo
        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        var consulta = new Consulta(null, medico, paciente, dados.data());

        consultaRepository.save(consulta);

    }

    // Método para escolher algum médico disponível, caso o médico não venha na requisição
    private Medico escolherMedico(DadosAgendamentoConsulta dados){

        if(dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for escolhido");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }
}
