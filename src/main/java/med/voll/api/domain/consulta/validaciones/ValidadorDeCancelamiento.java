package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.consulta.DatosConsultaCancelada;

public interface ValidadorDeCancelamiento {
    void validar(DatosConsultaCancelada datos);
}
