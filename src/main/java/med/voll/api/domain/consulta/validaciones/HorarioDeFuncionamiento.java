package med.voll.api.domain.consulta.validaciones;

import jakarta.validation.ValidationException;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;


@Component
public class HorarioDeFuncionamiento implements ValidadorDeConsultas {

    public void validar(DatosAgendarConsulta datos){

        var domingo = DayOfWeek.SUNDAY.equals(datos.data().getDayOfWeek());
        var antesDeApertura = datos.data().getHour()< 7;
        var despuesDeCierre = datos.data().getHour() > 19;

        if(domingo || antesDeApertura || despuesDeCierre){
            throw new ValidationException("El horario de atencion de la clinica es de lunes a sabado, de 07:00 a 19:00 horas");
        }


    }

}
