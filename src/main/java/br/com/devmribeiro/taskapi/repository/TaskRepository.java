package br.com.devmribeiro.taskapi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.db.utility.ConnFactory;

import br.com.devmribeiro.taskapi.config.Log;
import br.com.devmribeiro.taskapi.dto.TaskCreateDTO;
import br.com.devmribeiro.taskapi.dto.TaskUpdateDTO;
import br.com.devmribeiro.taskapi.model.Task;
import br.com.devmribeiro.taskapi.types.TaskPriority;
import br.com.devmribeiro.taskapi.types.TaskStatus;

public class TaskRepository {
	
	public List<Task> list(UUID taskId) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			conn = ConnFactory.open();
			
			String clause = taskId != null ? "where id = ('" + taskId + "'::uuid)" : "";
			
			ps = conn.prepareStatement("select * from task " + clause + " order by created_at");
			
			Log.i(ps.toString());
			
			rs = ps.executeQuery();
			
			List<Task> tasks = new ArrayList<Task>();
			while (rs.next()) {
				tasks.add(
					new Task(
						UUID.fromString(rs.getString("id")),
						rs.getString("title"),
						rs.getString("description"),
						TaskStatus.valueOf(rs.getString("status")),
						TaskPriority.valueOf(rs.getString("priority")),
						localDateTimeToString(rs.getObject("due_date", LocalDateTime.class)),
						localDateTimeToString(rs.getObject("created_at", LocalDateTime.class)),
						localDateTimeToString(rs.getObject("updated_at", LocalDateTime.class)))
				);
			}
			return tasks;
			
		} catch (Exception e) {
			Log.e("Erro ao realizar select", e);
		} finally {
			ConnFactory.close(rs, ps, conn);
		}
		return null;
	}
	
	public void create(TaskCreateDTO task) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			conn = ConnFactory.open();
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement(
					"insert into task (" +
					"	title, " +
					"	description, " + 
					" 	status, " +
					"	priority, " +
					"	due_date, " + 
					"	created_at, " + 
					"	updated_at " + 
					") values (?, ?, ?, ?, ?, now(), now())");
			
			ps.setString(1, task.title());
			ps.setString(2, task.description());
			ps.setString(3, TaskStatus.IN_PROGRESS.name());
			ps.setString(4, task.priority().toString());
			ps.setObject(5, task.dueDate());
			
			Log.i(ps.toString());
			
			if (ps.executeUpdate() == 1)
				conn.commit();
				
		} catch (Exception e) {
			Log.e("Erro ao inserir registro", e);
		} finally {
			ConnFactory.close(ps, conn);
		}
	}
	
	public void update(UUID taskId, TaskUpdateDTO task) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			conn = ConnFactory.open();
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement(
					"update task set " +
					"	title = ?, " +
					"	description = ?, " + 
					" 	status = ?, " +
					"	priority = ?, " +
					"	due_date = ?, " + 
					"	updated_at = now() " + 
					"where " +
					"	id = ?");
			
			ps.setString(1, task.title());
			ps.setString(2, task.description());
			ps.setString(3, task.status().toString());
			ps.setString(4, task.priority().toString());
			ps.setObject(5, task.dueDate());
			ps.setObject(6, taskId);
			
			Log.i(ps.toString());
			
			if (ps.executeUpdate() == 1)
				conn.commit();
				
		} catch (Exception e) {
			Log.e("Erro ao atualizar registro", e);
		} finally {
			ConnFactory.close(ps, conn);
		}
	}
	
	public void delete(UUID taskId) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			conn = ConnFactory.open();
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("delete from task where id = ?");
			ps.setObject(1, taskId);
			
			Log.i(ps.toString());
			
			if (ps.executeUpdate() == 1)
				conn.commit();
				
		} catch (Exception e) {
			Log.e("Erro ao remover registro", e);
		} finally {
			ConnFactory.close(ps, conn);
		}
	}
	
	private String localDateTimeToString(LocalDateTime dateTime) {
		return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : null;
	}
}