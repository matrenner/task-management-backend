import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { TaskService } from '../../services/task.service';
import { Task, TaskStatus } from '../../models/task.model';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule
  ]
})
export class TaskFormComponent {
  taskForm: FormGroup;
  statuses = Object.values(TaskStatus);
  isEditMode: boolean;

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private dialogRef: MatDialogRef<TaskFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Task | null
  ) {
    this.isEditMode = !!data;
    this.taskForm = this.fb.group({
      title: [data?.title || '', [Validators.required, Validators.maxLength(255)]],
      description: [data?.description || ''],
      status: [data?.status || TaskStatus.TODO],
      dueDate: [data?.dueDate || null]
    });
  }

  onSubmit(): void {
    if (this.taskForm.valid) {
      const task: Task = {
        ...this.taskForm.value,
        id: this.data?.id
      };

      const operation = this.isEditMode
        ? this.taskService.updateTask(this.data!.id!, task)
        : this.taskService.createTask(task);

      operation.subscribe({
        next: () => this.dialogRef.close(true),
        error: (error) => console.error('Error saving task:', error)
      });
    }
  }
}
