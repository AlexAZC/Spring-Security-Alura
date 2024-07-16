package med.voll.api.domain.consulta;

import med.voll.api.Medico;
import med.voll.api.domain.consulta.validaciones.HorarioDeAnticipacion;
import med.voll.api.domain.consulta.validaciones.ValidadorDeCancelamiento;
import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/* En esta clase implementaremos validaciones mas complejas
    que las que tenemos en BEAN VALIDATION */

@Service
public class AgendaDeConsultaService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ConsultaRepository consultaRepository;

    /* Creamos una lista de todos los archivos que contienen
        esa interfaz de VALIDADORCONSULTAS*/
    @Autowired
    List<ValidadorDeConsultas> validadores;
    /* Creamos otra lista pero con la interfaz de
        VALIDADORDECANCELAMIENTO*/
    @Autowired
    List<ValidadorDeCancelamiento> validadorDeCancelamientos;



    public DatosDetalleConsulta agendar(DatosAgendarConsulta datos){

        if(!pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("Esta id para el paciente no fue encontrado");
        }

        if(datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntegridad("Esta id para el medico no fue encontrado");
        }

        //Recorremos cada archivo mandando los datos de la lista
        validadores.forEach(v -> v.validar(datos));

        var paciente = pacienteRepository.findById(datos.idPaciente()).get();

        var medico = seleccionarMedico(datos);

        if(medico == null){
            throw new ValidacionDeIntegridad("No existen medicos disponibles para este horario o especialidad");
        }

        var consulta = new Consulta(medico , paciente, datos.data());
        consultaRepository.save(consulta);

        return new DatosDetalleConsulta(consulta);
    }



    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if(datos.idMedico() != null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }
        if(datos.especialidad() == null){
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para el medico");
        }

        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(), datos.data());
    }



    public void cancelarConsulta(DatosConsultaCancelada datos){

        if(!consultaRepository.existsById(datos.idConsulta())){
            throw new ValidacionDeIntegridad("Id de la consulta registrada no existe!");
        }

        validadorDeCancelamientos.forEach(v -> v.validar(datos));

        var consulta = consultaRepository.getReferenceById(datos.idConsulta());
        consulta.cancelar(datos.motivos());

    }



}
