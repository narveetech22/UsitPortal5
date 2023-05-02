package com.narvee.usit.service;

import java.util.List;
import com.narvee.usit.entity.Todo;
import com.narvee.usit.helper.GetRecruiter;
public interface ITodoService {
	public Todo save(Todo requirements);
	public List<GetRecruiter> getEMployees();
	public Todo update(Todo requirements);
	public Todo getByID(long reqID);
	public List<Todo> getAllTasks();
}
