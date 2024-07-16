package med.voll.api.domain.medico;

import med.voll.api.Medico;
import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.direccion.DatosDireccion;
import med.voll.api.domain.paciente.DatosRegistroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {


    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager entityManager;


    /* ESCENARIO 1*/
    @Test
    @DisplayName("deberia retornar nulo cuando el medico se encuentre en consulta con otro paciente en ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario1() {

        var proximoLunes10H = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);

        var medico = registrarMedico("Mesmmer","impaler@gmail.com", "451276",Especialidad.PEDIATRIA);
        var paciente= registrarPaciente("Mario","vargas@gmail.com","459219");
        registrarConsulta(medico, paciente, proximoLunes10H);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.PEDIATRIA,proximoLunes10H);

        assertThat(medicoLibre).isNull();

    }

    private void registrarConsulta(Medico medico, Paciente paciente, LocalDateTime data){
        entityManager.persist(new Consulta(medico, paciente, data));
    }

    private Medico registrarMedico(String nombre, String email, String documento, Especialidad especialidad){
        var medico = new Medico(datosMedico(nombre, email, documento, especialidad));
        entityManager.persist(medico);
        return medico;
    }

    /* Necesitamos crear un metodo que pase por el Record
       de DatosRegistroMedico ya que el constructor de
       Medico tiene como parametro este mismo record  */
    private DatosRegistroMedico datosMedico(String nombre, String email, String documento, Especialidad especialidad){
        return new DatosRegistroMedico(
                nombre,
                email,
                "313452",
                documento,
                especialidad,
                datosDireccion()
        );
    }

    /* En el record de DatosRegistroMedico necesitamos pasar
       como parametro la clase de DatosDireccion por ello
       tambien debemos crear un metodo */
    private DatosDireccion datosDireccion(){
        return new DatosDireccion(
                "La muerte",
                "Miraflores",
                "Arequipa",
                "32",
                "10"
        );
    }

    /* Al igual que los 2 anteriores metodos que creamos,
       lo mismo debemos hacer para la clase Paciente */
    private Paciente registrarPaciente(String nombre, String email, String documento){
        var paciente = new Paciente(datosPaciente(nombre, email,documento));
        entityManager.persist(paciente);
        return paciente;
    }


    private DatosRegistroPaciente datosPaciente(String nombre, String email, String documento){
        return new DatosRegistroPaciente(
                nombre,
                email,
                "124341",
                documento,
                datosDireccion()
        );
    }


    /* ESCENARIO 2*/
    @Test
    @DisplayName("deberia retornar un medico cuando realice la consulta en la base de datos para ese horario")
    void seleccionarMedicoConEspecialidadEnFechaEscenario2() {

        var proximoLunes10H = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);

        var medico = registrarMedico("Mesmmer","impaler@gmail.com", "451276",Especialidad.PEDIATRIA);

        var medicoLibre = medicoRepository.seleccionarMedicoConEspecialidadEnFecha(Especialidad.PEDIATRIA,proximoLunes10H);

        assertThat(medicoLibre).isEqualTo(medico);

    }









}