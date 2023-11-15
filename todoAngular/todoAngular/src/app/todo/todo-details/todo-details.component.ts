import {Component, Input} from '@angular/core';
import {Todo} from "../../model/Todo";
import {ActivatedRoute} from "@angular/router";
import {TodoService} from "../../services/todo.service";



@Component({
  selector: 'app-user-details',
  templateUrl: './todo-details.component.html',
  styleUrls: ['./todo-details.component.css']
})
export class TodoDetailsComponent {

  todo!: Todo
  mode: String = "Edit"


  constructor(
    private todoService: TodoService,
    private route: ActivatedRoute ) {
    const idTodo: Number = Number(this.route.snapshot.paramMap.get("id"))
    this.todoService.getTodoById(idTodo).subscribe(todo => this.todo = todo)
  }

  changeMode() {
    if (this.mode==="Edit") {
      this.mode = "Display"
    } else {
      this.mode = "Edit"
    }
  }

  updateTodo() {
    console.log("update")
    this.todoService.update(this.todo).subscribe(()=>this.changeMode());
  }

  deleteTodo() {
    console.log("delete")
    this.todoService.delete(this.todo).subscribe();
    window.location.href = '/todos';

  }

}
