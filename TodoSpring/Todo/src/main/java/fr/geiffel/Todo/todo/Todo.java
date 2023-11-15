package fr.geiffel.Todo.todo;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.sql.Timestamp;
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Todo {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private long ID;

    @JsonProperty("contenu")
    private String contenu;

    @JsonProperty("dateCreation")
    private Timestamp dateCreation;

    @Column(columnDefinition = "boolean default false")
    @JsonProperty("statut")
    private boolean statut; //Si True = accomplie si False == pas accomplie

    public Todo(long ID, String contenu, Timestamp dateCreation, boolean statut) {
        this.ID = ID;
        this.contenu = contenu;
        this.dateCreation = dateCreation;
        this.statut = statut;
    }

    public Todo(long ID) {
        this.ID = ID;
    }

    public Todo() {

    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public boolean isStatut() {
        return statut;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }
}
