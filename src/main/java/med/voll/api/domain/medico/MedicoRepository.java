package med.voll.api.domain.medico;

import med.voll.api.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface MedicoRepository extends JpaRepository<Medico,Long> {
    Page<Medico> findByActivoTrue(Pageable paginacion);

    @Query("""
             select m from Medico m
                         where m.activo= true\s
                         and
                         m.especialidad=:especialidad\s
                         and
                         m.id not in( \s
                             select c.medico.id from Consulta c
                             where
                             c.data=:data
                         )
                         ORDER BY RANDOM()
                         limit 1
           """)
    Medico seleccionarMedicoConEspecialidadEnFecha(Especialidad especialidad, LocalDateTime data);


    @Query("""
            select m.activo from Medico m
            where m.id=:idmedico
            """)
    Boolean findActivoById(Long idmedico);
}
