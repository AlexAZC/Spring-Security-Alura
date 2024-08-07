package med.voll.api.controller;

import med.voll.api.domain.consulta.AgendaDeConsultaService;
import med.voll.api.domain.consulta.DatosAgendarConsulta;
import med.voll.api.domain.consulta.DatosDetalleConsulta;
import med.voll.api.domain.medico.Especialidad;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class ConsultaControllerTest {

    @Autowired
    private MockMvc mvc;

    //Esta clase convierte Atributos de Objetos Java a Atributos de JSON
    @Autowired
    private JacksonTester<DatosAgendarConsulta> agendarConsultaJacksonTester;


    //Esta clase convierte Atributos JSON a atributos de Objetos Java
    @Autowired
    private JacksonTester<DatosDetalleConsulta> detalleConsultaJacksonTester;

    //Esta clase sirve para simular un service en este caso seria el de AgendaConsultaService
    @MockBean
    private AgendaDeConsultaService agendaDeConsultaService;

    @Test
    @DisplayName("deberia retornar estado http:400 cuando los datos ingresados sean invalidos")
    @WithMockUser
    void agendarEscenario1() throws Exception {

        //given //when
        var response = mvc.perform(post("/consultas")).andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("deberia retornar estado http:200 cuando los datos ingresados son validos")
    @WithMockUser
    void agendarEscenario2() throws Exception {

        //given

        var fecha = LocalDateTime.now().plusHours(1);
        var especialidad = Especialidad.CARDIOLOGIA;
        var datos = new DatosDetalleConsulta(null, 2L, 5L , fecha);


        // when
        when(agendaDeConsultaService.agendar(any())).thenReturn(datos);


        var response = mvc.perform(post("/consultas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(agendarConsultaJacksonTester.write(new DatosAgendarConsulta(2L, 4L, fecha, especialidad )).getJson()))
                .andReturn().getResponse()
                ;

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        var jsonEsperado = detalleConsultaJacksonTester.write(datos).getJson();

        assertThat(response.getContentAsString()).isEqualTo(jsonEsperado);
    }


}