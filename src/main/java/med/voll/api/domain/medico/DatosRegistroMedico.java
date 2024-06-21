package med.voll.api.domain.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.direccion.DatosDireccion;

/***** DEPENDENCIA PATTERN *****/
/* Sirve para poder crear validaciones a atributos o llamadas
    de clases, como para que no sean nulos o que no esten en blanco
      o incluso para poder crear patrones como que solo reciba numeros
       o que solo reciba simbolos,etc*/


public record DatosRegistroMedico(
       @NotBlank
       String nombre,
       @NotBlank
       @Email
       String email,
       @NotBlank
       String telefono,
       @NotBlank
       @Pattern(regexp = "\\d{4,6}")
       String documento,
       @NotNull //Se utiliza NotBlank para atributos
       Especialidad especialidad,
       @NotNull //Se utiliza NotNull para clases o Enums
       @Valid
       DatosDireccion direccion


) {
}
