package fr.geiffel.Todo.todo;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.geiffel.Todo.exceptions.ExceptionHandlingAdvice;
import fr.geiffel.Todo.exceptions.ResourceNotFoundException;
import fr.geiffel.Todo.exceptions.ResourceAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = TodoController.class)
@Import(ExceptionHandlingAdvice.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoJPAService todoService;

    private List<Todo> todos;

    ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        todos = new ArrayList<>(){{
            add(new Todo(1L,"Content1", Timestamp.from(Instant.now()), false));
            add(new Todo(2L,"Content2",Timestamp.from(Instant.now()), false));
            add(new Todo(3L,"Content3",Timestamp.from(Instant.now()), false));
            add(new Todo(7L,"Content7",Timestamp.from(Instant.now()), false));


        }};

    }



    @Test
    void TestGetAll() throws Exception {
        when(todoService.getAll()).thenReturn(todos);

        mockMvc.perform(get("/todos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)))
                .andDo(print());
    }

    @Test
    void TestGetById() throws Exception {
        Todo todo = todos.get(0);
        when(todoService.getById(any())).thenReturn(todo);

        mockMvc.perform(get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(content().contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()
        ).andExpect(jsonPath("$", hasEntry("contenu", todo.getContenu()))
        ).andDo(print());
    }


    @Test
    void TestGetByIdError() throws Exception {
        when(todoService.getById(any())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/todos/101"))
                .andExpect(status().isNotFound());
    }

    @Test
    void TestCreate() throws Exception {
        Todo todo = new Todo(10L,"Content", Timestamp.from(Instant.now()), false);
        todo.setID(4L);
        when(todoService.create(any())).thenReturn(todo);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(header().string("Location", "/todos/"+todo.getID()))
                .andDo(print());
    }

    @Test
    void TestCreateError() throws Exception {
        Todo todo = todos.get(1);
        when(todoService.create(any())).thenThrow(ResourceAlreadyExistsException.class);

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(todo)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void TestUpdate() throws Exception {
        Todo todo = new Todo(10L,"Content", Timestamp.from(Instant.now()), false);
        Mockito.when(todoService.create(Mockito.any(Todo.class))).thenReturn(todo);


        String toSend = mapper.writeValueAsString(todo);

        mockMvc.perform(post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toSend)
        ).andExpect(status().isCreated()
        ).andExpect(header().string("Location","/todos/"+todo.getID())
        ).andDo(print()).andReturn();
    }

    @Test
    void TestUpdateError() throws Exception {
        Todo todo = todos.get(0);
        todo.setID(12L);

        ArgumentCaptor<Todo> todo_received = ArgumentCaptor.forClass(Todo.class);

        mockMvc.perform(put("/todos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo))
        ).andExpect(status().isNoContent()
        ).andDo(print()).andReturn();

        Mockito.verify(todoService).update(Mockito.anyLong(), todo_received.capture());
        assertAll(
                ()-> assertEquals(todo.getID(), todo_received.getValue().getID()),
                ()-> assertEquals(todo.getContenu(), todo_received.getValue().getContenu()),
                ()-> assertEquals(todo.isStatut(), todo_received.getValue().isStatut())
        );

    }

    @Test
    void TestDelete() throws Exception {
        Long code_toSend = 1L;

        mockMvc.perform(delete("/todos/"+code_toSend)
        ).andExpect(status().isNoContent()
        ).andDo(print()).andReturn();

        ArgumentCaptor<Long>  id_received = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(todoService).delete(todoService.getById(id_received.capture()));
        assertEquals(code_toSend, id_received.getValue());
    }


    @Test
    void TestDeleteError() throws Exception {
        Mockito.doThrow(ResourceNotFoundException.class).when(todoService).delete(todoService.getById(Mockito.anyLong()));

        mockMvc.perform(delete("/todos/5412")
        ).andExpect(status().isNotFound()
        ).andDo(print()).andReturn();
    }
}
