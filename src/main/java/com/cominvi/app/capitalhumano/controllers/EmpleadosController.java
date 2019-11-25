package com.cominvi.app.capitalhumano.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.cominvi.app.capitalhumano.services.IEmpleadosService;
import com.cominvi.app.commons.models.Empleados;

@RestController
@RequestMapping("/empleado")
public class EmpleadosController {

	
	@Autowired
	private IEmpleadosService empleadoService;
	
	@GetMapping()
	public List<Empleados> index() {
		return empleadoService.findAll();
	}
	
	
	@GetMapping("/{idempleado}")
	public Empleados buscarEmpleados(@PathVariable Long idempleado) {
		
		Empleados empleado = null;
		
		try {
			empleado = empleadoService.findById(idempleado);
		}catch (DataAccessException e) {
			return null;
		}
		
		return empleado;
	}
	

	
	@PostMapping()
	public ResponseEntity<?> guardar(@RequestBody Empleados empleado) {

		Map<String, Object> map = new HashMap<>();
		try {
			empleado = empleadoService.save(empleado);
		}catch (DataAccessException e) {
			map.put("mensaje", "Error al insertar el registro");
			map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Empleados>(empleado, HttpStatus.CREATED);
		
	}
	
	@PutMapping("/{idempleado}")
	public ResponseEntity<?> actualizar(@RequestBody Empleados empleado, @PathVariable Long idempleado) {
		
		Map<String, Object> map = new HashMap<>();
		try {
			empleadoService.update(empleado, idempleado);
			empleado = empleadoService.findById(idempleado);
		}catch (DataAccessException e) {
			map.put("mensaje", "Error al insertar el registro");
			map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<Empleados>(empleado, HttpStatus.CREATED);
		
	}
	
	@DeleteMapping("/{idempleado}")
	public ResponseEntity<?> eliminarEmpleados(@PathVariable Long idempleado) {
		
		Map<String, Object> map = new HashMap<>();
		try {
			empleadoService.removeById(idempleado);
		}catch (DataAccessException e) {
			map.put("mensaje", "Error al insertar el registro");
			map.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		map.put("mensaje", "El registro se elimino con �xito");
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
		
	}
	
	
}
