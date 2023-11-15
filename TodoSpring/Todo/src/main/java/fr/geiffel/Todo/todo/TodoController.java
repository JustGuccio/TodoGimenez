package fr.geiffel.Todo.todo;

import org.hibernate.annotations.Bag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoJPAService todoService;
    @Autowired
    public TodoController(TodoJPAService todoService) {
        this.todoService = todoService;
    }


    @GetMapping("")
    public List<Todo> getAll(){
        return todoService.getAll();
    }

    @GetMapping("/{id}")
    public Todo getById(@PathVariable Long id){
        return todoService.getById(id);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody Todo todo){
        Todo created_todo = todoService.create(todo);
        return ResponseEntity.created(URI.create("/todos/" + created_todo.getID())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Todo todo) {
        todoService.update(id, todo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Todo toDelete = todoService.getById(id);
        todoService.delete(toDelete);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
