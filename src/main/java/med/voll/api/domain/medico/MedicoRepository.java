package med.voll.api.domain.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Page<Medico> findAllByAtivoTrue(Pageable page);

    @Query("""
    select m.ativo from Medico m
    where
    m.id = :idMedico
    """)
    Boolean findAtivoById(Long idMedico);

    @Query("""
    select m from Medico m
    where\s
    m.ativo = true
    and
    m.especialidade = :especialidade
    and
    m.id not in(
        select c.medico.id from Consulta c
        where\s
        c.data = :data
    )
    order by randon()
    limit 1
    """)
    Medico escolherMedicoAleatorioLivreNaData(Especialidade especialidade, LocalDateTime data);
}
