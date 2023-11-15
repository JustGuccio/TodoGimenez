package fr.geiffel.Todo.todo;

import fr.geiffel.Todo.exceptions.ResourceAlreadyExistsException;
import fr.geiffel.Todo.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TodoJPAService implements TodoService{

    @Autowired
    private TodoRepository todoRepository;
    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getById(Long id) {
        Optional<Todo> todo = todoRepository.findById(id);
        if (todo.isPresent()) {
            return todo.get();
        } else {
            throw new ResourceNotFoundException("Todo", id);
        }
    }

    @Override
    public Todo create(Todo newTodo) {
        Long id = newTodo.getID();
        if(todoRepository.existsById(id)){
            throw new ResourceAlreadyExistsException("Todo", id);
        } else {
            todoRepository.save(newTodo);
        }
        return newTodo;
    }


    @Override
    public Todo update(Long id, Todo updatedTodo) {
        if(todoRepository.existsById(id)){
            return todoRepository.save(updatedTodo);
        }else {
            throw new ResourceNotFoundException("Todo" , id);
        }
    }

    @Override
    public void delete(Todo toDelete) {
        Long id = toDelete.getID();
        if(!todoRepository.existsById(id)){
            throw new ResourceNotFoundException("Todo" , id);
        }
        else {
            todoRepository.delete(toDelete);

        }

    }
}
