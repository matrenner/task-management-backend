import { Component, OnInit } from '@angular/core';
import { TaskService } from '../../services/task.service';
import { Task, TaskStatus } from '../../models/task.model';
import { MatDialog } from '@angular/material/dialog';
import { TaskFormComponent } from '../task-form/task-form.component';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnInit {
  tasks: Task[] = [];
  TaskStatus = TaskStatus;

  constructor(
    private taskService: TaskService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(status?: TaskStatus): void {
    this.taskService.getTasks(status).subscribe(tasks => {
      this.tasks = tasks;
    });
  }

  openTaskForm(task?: Task): void {
    const dialogRef = this.dialog.open(TaskFormComponent, {
      width: '500px',
      data: task
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadTasks();
      }
    });
  }

  deleteTask(id: string): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(id).subscribe(() => {
        this.loadTasks();
      });
    }
  }

  getStatusChipColor(status: TaskStatus): string {
    const colors = {
      [TaskStatus.TODO]: 'primary',
      [TaskStatus.IN_PROGRESS]: 'accent',
      [TaskStatus.COMPLETED]: 'primary',
      [TaskStatus.CANCELLED]: 'warn'
    };
    return colors[status];
  }
}
