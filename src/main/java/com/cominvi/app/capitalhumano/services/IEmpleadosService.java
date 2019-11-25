package com.cominvi.app.capitalhumano.services;

import java.util.List;
import com.cominvi.app.commons.models.Empleados;

public interface IEmpleadosService {

	List<Empleados> findAll();
	Empleados findById(Long idempleado);
	Empleados save(Empleados empleado);
	Boolean saveAll(List<Empleados> empleados);
	Empleados update(Empleados empleado, Long idempleado);
	Boolean removeById(Long idempleado);

}
