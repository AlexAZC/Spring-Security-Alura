package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosConsultaCancelada;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;


@Component
public class HorarioPeresozo implements ValidadorDeCancelamiento{

    @Autowired
    private ConsultaRepository consultaRepository;


    @Override
    public void validar(DatosConsultaCancelada datos) {
        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        var ahora = LocalDateTime.now();
        var diferenciaDeHoras = Duration.between(ahora, consulta.getData()).toHours();

        if(diferenciaDeHoras < 24){
            throw new ValidationException("Consulta solamente puede ser cancelada con antecedencia mínima de 24h!");
        }

    }
}
