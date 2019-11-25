package com.cominvi.app.capitalhumano.services.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cominvi.app.commons.models.Empleados;
import com.cominvi.app.capitalhumano.repositories.JdbcEmpleadosRepository;
import com.cominvi.app.capitalhumano.services.IEmpleadosService;


@Service
public class EmpleadosService implements IEmpleadosService{
	@Autowired
	private JdbcEmpleadosRepository empleadoDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Empleados> findAll() {
		return empleadoDao.findAll();
	}

	@Transactional(readOnly = true)
	@Override
	public Empleados findById(Long idempleado) {
		return empleadoDao.findById(idempleado);
	}

	@Transactional
	@Override
	public Empleados save(Empleados empleado) {
		return empleadoDao.save(empleado);
	}

	@Transactional
	@Override
	public Boolean saveAll(List<Empleados> empleados) {
		return empleadoDao.saveAll(empleados);
	}

	@Transactional
	@Override
	public Empleados update(Empleados empleado, Long idempleado) {
		return empleadoDao.update(empleado, idempleado);
	}

	@Transactional
	@Override
	public Boolean removeById(Long idempleado) {
		return empleadoDao.removeById(idempleado);
	}

}
