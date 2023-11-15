package fr.geiffel.Todo.todo;

import fr.geiffel.Todo.exceptions.ResourceAlreadyExistsException;
import fr.geiffel.Todo.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration(classes = TodoJPAService.class)
public class TodoServiceTest {

    @Autowired
    private TodoService todoService;
    private List<Todo> todos;

    @MockBean
    private TodoRepository todoRepository;

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(){{
            add(new Todo(1L,"Content",Timestamp.from(Instant.now()), false));
            add(new Todo(2L,"Content",Timestamp.from(Instant.now()), false));
            add(new Todo(3L,"Content",Timestamp.from(Instant.now()), false));
            add(new Todo(7L,"Content",Timestamp.from(Instant.now()), false));


        }};

        }


    @Test
    void testGetAll_renvoie4(){
        when(todoRepository.findAll()).thenReturn(todos);
        assertEquals(4 , todoService.getAll().size());
    }

    @Test
    void testGetById(){
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todos.get(0)));
        when(todoRepository.findById(12L)).thenReturn(Optional.empty());
        assertAll(
                ()-> assertEquals(todos.get(0), todoService.getById(1L)),
                ()-> assertThrows(ResourceNotFoundException.class, ()-> todoService.getById(12L))
        );


    }

    @Test
    void testCreation(){
        Todo todo = new Todo(5L, "tnetnoC", Timestamp.from(Instant.now()), false);
        //Todo todo2 = new Todo(1L, "tnetnoC", Timestamp.from(Instant.now()), false);
        when(todoRepository.save(any(Todo.class))).thenReturn(todo);
        //when(todoRepository.exists(Example.of(todo))).thenReturn(true);
        assertAll(
                ()-> assertEquals(todo, todoService.create(todo))//,
                //()-> assertThrows(ResourceAlreadyExistsException.class, ()-> todoService.create(todo))
        );
    }

    @Test
    void testUpdate(){
        Todo expected_todo = todos.get(0);
        expected_todo.setContenu("Modified");

        when(todoRepository.existsById(expected_todo.getID())).thenReturn(true);
        when(todoRepository.save(any(Todo.class))).thenReturn(expected_todo);

        assertEquals(expected_todo, todoService.update(expected_todo.getID(), expected_todo));
    }

    @Test
    void testUpdateError(){
        Todo todo = new Todo(8L,"Update content", Timestamp.from(Instant.now()), true);
        todo.setID(4L);
        when(todoRepository.exists(Example.of(todo))).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> todoService.update(todo.getID(), todo));
    }

    @Test
    void testDelete(){
        Todo toDelete = todos.get(0);
        when(todoRepository.existsById(toDelete.getID())).thenReturn(true);
        todoService.delete(toDelete);
        verify(todoRepository).delete(toDelete);
    }

    @Test
    void testDeleteError(){
        Todo toDelete = new Todo(145L,  "New", Timestamp.from(Instant.now()), true);
        doThrow(ResourceNotFoundException.class).when(todoRepository).delete(any());

        assertThrows(ResourceNotFoundException.class, () -> todoService.delete(toDelete));
    }




}
