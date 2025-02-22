import { Component, OnInit } from '@angular/core';
import { Task, TaskStatus } from '../../models/task.model';
import { TaskService } from '../../services/task.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
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
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(status?: TaskStatus): void {
    this.taskService.getTasks(status).subscribe({
      next: (tasks) => this.tasks = tasks,
      error: (error) => this.showError('Error loading tasks')
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
        this.showSuccess(task ? 'Task updated' : 'Task created');
      }
    });
  }

  deleteTask(task: Task): void {
    if (confirm('Are you sure you want to delete this task?')) {
      this.taskService.deleteTask(task.id!).subscribe({
        next: () => {
          this.loadTasks();
          this.showSuccess('Task deleted');
        },
        error: () => this.showError('Error deleting task')
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

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', { duration: 3000 });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', { 
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}
