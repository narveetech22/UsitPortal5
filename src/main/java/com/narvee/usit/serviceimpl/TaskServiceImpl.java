package com.narvee.usit.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.AssignRequirement;
import com.narvee.usit.entity.TaskAssign;
import com.narvee.usit.entity.Todo;
import com.narvee.usit.helper.GetRecruiter;
import com.narvee.usit.repository.ITaskAssnRepository;
import com.narvee.usit.repository.ITaskRepository;
import com.narvee.usit.service.ITodoService;

@Service
public class TaskServiceImpl implements ITodoService {

	@Autowired
	private ITaskRepository todorepo;

	@Autowired
	private ITaskAssnRepository taskassnrepo;

	@Override
	public Todo save(Todo todo) {
		Todo task = todorepo.save(todo);
//		todo.getAsnTask().forEach(rafVar -> {
//			TaskAssign assgnTask = new TaskAssign();
//			assgnTask.setTaskid(task.getId());
//			assgnTask.setUserid(rafVar.getUserid());
//			assgnTask.setFullname(rafVar.getFullname());
//			taskassnrepo.save(assgnTask);
//		});
		return task;
	}

	@Override
	public Todo update(Todo requirements) {
		return null;
	}

	@Override
	public Todo getByID(long reqID) {
		return null;
	}

	@Override
	public List<Todo> getAllTasks() {
		return todorepo.findAll();
	}

	@Override
	public List<GetRecruiter> getEMployees() {
		// TODO Auto-generated method stub
		return todorepo.getEmployees();
	}

}
