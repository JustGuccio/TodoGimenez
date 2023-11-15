package fr.geiffel.Todo.todo;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //Normalement il est pas là mais je le laisse ici car ça ne change rien mais c'est pour bien me rappeler qu'il ne doit pas être la
public interface TodoService {
    List<Todo> getAll();

    Todo getById(Long id);

    Todo create(Todo newTodo);

    Todo update(Long id ,Todo updatedTodo);

    void delete(Todo toDelete);
}
