package com.codedecode.demo.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.codedecode.demo.exception.BadRequestException;
import com.codedecode.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codedecode.demo.entity.Employee;
import com.codedecode.demo.repos.EmployeeCrudRepo;

@Service
public class EmployeeService implements EmployeeServiceInterface{
	
	@Autowired
	private EmployeeCrudRepo crudRepo;

	@Override
	public Employee addEmployee(Employee employee) {
		if (employee.getName().isEmpty() || employee.getName().length() == 0) {
		throw  new BadRequestException("Missing arguments");
		}
		Employee savedEmployee = crudRepo.save(employee);
		return savedEmployee;
		
	}

	@Override
	public List<Employee> getAllEmployees() {
		return crudRepo.findAll();
	}

	@Override
	public Employee getEmpById(Long empidL) {
		Optional<Employee> employee = crudRepo.findById(empidL);
		if (!employee.isPresent()) {
			throw  new NotFoundException("Employee with id " + empidL +  " not found");
		}
		return employee.get();
		//return crudRepo.findById(empidL).get();
	}

	@Override
	public void deleteEmpById(Long empidL) {
		Employee employee = getEmpById(empidL);

		//crudRepo.delete(employee);
		crudRepo.deleteById(employee.getId());
		//crudRepo.deleteById(empidL);
	}

}
