package fr.geiffel.Todo;

import fr.geiffel.Todo.todo.Todo;
import fr.geiffel.Todo.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan({"fr.geiffel.Todo.todo"})
public class TodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	@Autowired
	private TodoRepository todoRepository;

	@Bean
	public CommandLineRunner setUpBDD() {
		return (args) -> {
			List<Todo> todos = new ArrayList<>() {{
				add(new Todo(1L, "Content1", Timestamp.from(Instant.now()), false));
				add(new Todo(4L, "Content1", Timestamp.from(Instant.now()), false));
				add(new Todo(5L, "Content1", Timestamp.from(Instant.now()), false));
			}};
			todoRepository.saveAll(todos);
		};
	}
}