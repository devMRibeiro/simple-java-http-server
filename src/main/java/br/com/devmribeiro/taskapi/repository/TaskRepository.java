package br.com.devmribeiro.taskapi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
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
	
	public Task findById(UUID userId, UUID id) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			conn = ConnFactory.open();
			
			ps = conn.prepareStatement("select * from task where user_id = ? and id = ?");
			ps.setObject(1, userId);
			ps.setObject(2, id);
			
			Log.i(ps);
			
			rs = ps.executeQuery();
			
			if (rs.next()) {
				Task task = new Task(
						UUID.fromString(rs.getString("id")),
						rs.getString("title"),
						rs.getString("description"),
						TaskStatus.valueOf(rs.getString("status")),
						TaskPriority.valueOf(rs.getString("priority")),
						rs.getObject("due_date", LocalDateTime.class),
						rs.getObject("created_at", LocalDateTime.class),
						rs.getObject("updated_at", LocalDateTime.class),
						UUID.fromString(rs.getString("user_id"))
				);
				return task;
			}
			
		} catch (Exception e) {
			Log.e("Erro ao realizar select", e);
		} finally {
			ConnFactory.close(rs, ps, conn);
		}
		return null;
	}

	public List<Task> list(UUID userId) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			conn = ConnFactory.open();
			
			ps = conn.prepareStatement("select * from task where user_id = ? order by created_at");
			ps.setObject(1, userId);
			
			Log.i(ps);
			
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
						rs.getObject("due_date", LocalDateTime.class),
						rs.getObject("created_at", LocalDateTime.class),
						rs.getObject("updated_at", LocalDateTime.class),
						UUID.fromString(rs.getString("user_id")))
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
					"	updated_at, " + 
					"	user_id " +
					") values (?, ?, ?, ?, ?, now(), now(), ?)");
			
			ps.setString(1, task.title());
			ps.setString(2, task.description());
			ps.setString(3, task.status().toString());
			ps.setString(4, task.priority().toString());
			ps.setObject(5, task.dueDate());
			ps.setObject(6, task.userId());
			
			Log.i(ps);
			
			if (ps.executeUpdate() == 1)
				conn.commit();
				
		} catch (Exception e) {
			Log.e("Erro ao inserir registro", e);
		} finally {
			ConnFactory.close(ps, conn);
		}
	}
	
	public void update(TaskUpdateDTO task) {
		
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
					"	updated_at = now(), " + 
					"	user_id = ? " +
					"where " +
					"	id = ?");
			
			ps.setString(1, task.title());
			ps.setString(2, task.description());
			ps.setString(3, task.status().toString());
			ps.setString(4, task.priority().toString());
			ps.setObject(5, task.dueDate());
			ps.setObject(6, task.userId());
			ps.setObject(7, task.id());
			
			Log.i(ps);
			
			if (ps.executeUpdate() == 1)
				conn.commit();
				
		} catch (Exception e) {
			Log.e("Erro ao atualizar registro", e);
		} finally {
			ConnFactory.close(ps, conn);
		}
	}
	
	public void delete(UUID taskId, UUID userId) {
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			
			conn = ConnFactory.open();
			conn.setAutoCommit(false);
			
			ps = conn.prepareStatement("delete from task where id = ? and user_id = ?");
			ps.setObject(1, taskId);
			ps.setObject(2, userId);
			
			Log.i(ps);
			
			if (ps.executeUpdate() == 1)
				conn.commit();
				
		} catch (Exception e) {
			Log.e("Erro ao remover registro", e);
		} finally {
			ConnFactory.close(ps, conn);
		}
	}
}