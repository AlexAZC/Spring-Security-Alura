package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DatosConsultaCancelada(
        @NotNull
        Long idConsulta,
        @NotNull
        Motivos motivos
) {

}
