import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { TaskService } from '../../services/task.service';
import { Task, TaskStatus } from '../../models/task.model';
import { TaskFormComponent } from '../task-form/task-form.component';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDialogModule,
    MatSnackBarModule
  ]
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

  loadTasks(): void {
    this.taskService.getTasks().subscribe({
      next: (tasks) => {
        console.log(tasks);
        this.tasks = tasks;
      },
      error: (error) => {
        console.log('loadTasks');
        console.error(error);
      },
    });
  }

  loadTasksByStatus(status?: TaskStatus): void {
    this.taskService.getTasksByStatus(status).subscribe({
      next: (tasks) => {
        console.log(tasks);
        this.tasks = tasks;
      },
      error: (error) => {
        console.log('loadTasksByStatus');
        console.error(error);
      },
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
