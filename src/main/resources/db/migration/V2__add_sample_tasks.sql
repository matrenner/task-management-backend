-- Add sample tasks with different statuses and dates
INSERT INTO tasks (id, title, description, status, due_date, created_at, updated_at) VALUES
    (
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'Complete Project Documentation',
        'Write comprehensive documentation for the task management system',
        'TODO',
        NOW() + INTERVAL '7 days',
        NOW(),
        NOW()
    ),
    (
        'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a22',
        'Implement User Authentication',
        'Add user login and registration features',
        'IN_PROGRESS',
        NOW() + INTERVAL '3 days',
        NOW() - INTERVAL '2 days',
        NOW()
    ),
    (
        'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a33',
        'Fix Navigation Bug',
        'Resolve issue with menu navigation in mobile view',
        'COMPLETED',
        NOW() - INTERVAL '1 day',
        NOW() - INTERVAL '5 days',
        NOW() - INTERVAL '1 day'
    ),
    (
        'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a44',
        'Review Pull Requests',
        'Review and merge pending pull requests',
        'TODO',
        NOW() + INTERVAL '1 day',
        NOW(),
        NOW()
    ),
    (
        'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a55',
        'Update Dependencies',
        'Update all project dependencies to latest versions',
        'CANCELLED',
        NOW() - INTERVAL '2 days',
        NOW() - INTERVAL '7 days',
        NOW() - INTERVAL '2 days'
    );
