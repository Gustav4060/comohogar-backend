package com.comohogar.pruebabackend.controlador;

import com.comohogar.pruebabackend.dto.ClienteRespuestaDto;
import com.comohogar.pruebabackend.excepciones.ModeloNotFoundException;
import com.comohogar.pruebabackend.excepciones.MovimientosException;
import com.comohogar.pruebabackend.servicio.IClienteServicio;
import com.comohogar.pruebabackend.dto.ClienteDto;
import com.comohogar.pruebabackend.entidad.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteControlador {
    public static final String ID_NO_ENCONTRADO = "ID NO ENCONTRADO ";

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private IClienteServicio clienteServicio;

    /**
     * Obtiene la lista de Clientes
     *
     * @return -dto de Clientes
     * @throws Exception
     */
    @GetMapping
    public ResponseEntity<List<ClienteDto>> listar() throws MovimientosException {
        List<ClienteDto> lista = clienteServicio.listar().stream().map(p -> mapper.map(p, ClienteDto.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * Lista por identificador de Cliente
     *
     * @param id identificador del Cliente
     * @return Dto de Cliente
     * @throws Exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> listarPorId(@PathVariable("id") Long id) throws MovimientosException {
        ClienteDto dtoResponse;
        Cliente obj = clienteServicio.listarPorId(id);
        if (obj == null) {
            throw new ModeloNotFoundException(ID_NO_ENCONTRADO+ id);
        } else {
            dtoResponse = mapper.map(obj, ClienteDto.class); //
        }
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    /**
     * Registra el Cliente
     *
     * @param dtoRequest dto del Cliente
     * @return Cliente dto
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<ClienteDto> registrar(@Valid @RequestBody ClienteDto dtoRequest) throws MovimientosException {
        Cliente c = mapper.map(dtoRequest, Cliente.class);
        Cliente obj = clienteServicio.registrar(c);
        ClienteDto dtoResponse = mapper.map(obj, ClienteDto.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.CREATED);
    }

    /**
     * Modifica el Cliente
     *
     * @param dtoRequest deto de Cliente
     * @return dto de Cliente modificado
     * @throws Exception
     */
    @PutMapping
    public ResponseEntity<ClienteDto> modificar(@RequestBody ClienteDto dtoRequest) throws MovimientosException {
        Cliente cliente = clienteServicio.listarPorId(dtoRequest.getId());
        if (cliente == null) {
            throw new ModeloNotFoundException(ID_NO_ENCONTRADO+ dtoRequest.getId());
        }
        Cliente p = mapper.map(dtoRequest, Cliente.class);
        Cliente obj = clienteServicio.modificar(p);
        ClienteDto dtoResponse = mapper.map(obj, ClienteDto.class);
        return new ResponseEntity<>(dtoResponse, HttpStatus.OK);
    }

    /**
     * Elimina Cliente por id
     *
     * @param id identificador del Cliente
     * @return retorna vacio
     * @throws Exception
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) throws MovimientosException {
        Cliente cliente = clienteServicio.listarPorId(id);
        if (cliente == null) {
            throw new ModeloNotFoundException(ID_NO_ENCONTRADO+ id);
        }
        clienteServicio.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PostMapping("/registraValida")
    public ResponseEntity<ClienteRespuestaDto> registrarCliente(@Valid @RequestBody ClienteDto dtoRequest) throws MovimientosException {
        ClienteRespuestaDto c = mapper.map(dtoRequest, ClienteRespuestaDto.class);
        ClienteRespuestaDto obj = clienteServicio.registrarValida(c);
        return new ResponseEntity<>(obj, HttpStatus.CREATED);
    }
}
