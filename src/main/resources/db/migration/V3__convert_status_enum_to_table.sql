-- Create temporary backup of tasks
CREATE TABLE tasks_backup AS SELECT * FROM tasks;

-- Create the new status table
CREATE TABLE task_statuses (
    name VARCHAR(50) PRIMARY KEY,
    display_name VARCHAR(100) NOT NULL,
    description TEXT,
    order_position INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert the status values
INSERT INTO task_statuses (name, display_name, description, order_position) VALUES
    ('TODO', 'To Do', 'Task that needs to be started', 1),
    ('IN_PROGRESS', 'In Progress', 'Task that is currently being worked on', 2),
    ('COMPLETED', 'Completed', 'Task that has been finished', 3),
    ('CANCELLED', 'Cancelled', 'Task that has been cancelled', 4);

-- Add new status column
ALTER TABLE tasks ADD COLUMN status_new VARCHAR(50);

-- Convert existing data
UPDATE tasks 
SET status_new = status::text 
WHERE status IS NOT NULL;

-- Verify data conversion
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 
        FROM tasks 
        WHERE status IS NOT NULL 
        AND status_new IS NULL
    ) THEN
        RAISE EXCEPTION 'Data conversion failed - some status values were not converted';
    END IF;
END $$;

-- Add foreign key constraint
ALTER TABLE tasks 
    ADD CONSTRAINT fk_task_status 
    FOREIGN KEY (status_new) 
    REFERENCES task_statuses(name);

-- Drop old column and rename new one
ALTER TABLE tasks DROP COLUMN status;
ALTER TABLE tasks RENAME COLUMN status_new TO status;

-- Make status non-nullable
ALTER TABLE tasks ALTER COLUMN status SET NOT NULL;

DROP TABLE tasks_backup;
-- Drop the enum type
DROP TYPE task_status;
