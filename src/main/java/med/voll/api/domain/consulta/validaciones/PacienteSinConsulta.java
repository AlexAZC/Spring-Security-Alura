package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class PacienteSinConsulta implements ValidadorDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;


    public void validar(DatosAgendarConsulta datos){

        var primerHorario = datos.data().withHour(7);

        var ultimoHorario = datos.data().withHour(18);

        var pacienteConsulta = consultaRepository.existsByPacienteIdAndDataBetween(datos.idPaciente(), primerHorario, ultimoHorario);

        if(pacienteConsulta){
            throw new ValidationException("El paciente ya tiene una consulta para ese dia");
        }

    }

}
