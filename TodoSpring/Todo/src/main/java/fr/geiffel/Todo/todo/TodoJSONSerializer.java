package fr.geiffel.Todo.todo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import fr.geiffel.Todo.URL;

import java.io.IOException;

public class TodoJSONSerializer extends JsonSerializer<Todo> {
    @Override
    public void serialize(Todo todo, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("Contenu" , todo.getContenu());
        gen.writeStringField("URL" , new URL().getDomaineName()+"/todos/"+ todo.getID());
        gen.writeEndObject();
    }
}
