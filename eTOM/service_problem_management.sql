-- Create the database
CREATE DATABASE IF NOT EXISTS service_problem_management;
USE service_problem_management;

-- Create service_problems table
CREATE TABLE IF NOT EXISTS service_problems (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL,
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL,
    category VARCHAR(100),
    status ENUM('NEW', 'ACKNOWLEDGED', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    created_by VARCHAR(100),
    assigned_to VARCHAR(100),
    related_service_id VARCHAR(36),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_created_at (created_at)
);

-- Create problem_acknowledgments table
CREATE TABLE IF NOT EXISTS problem_acknowledgments (
    id VARCHAR(36) PRIMARY KEY,
    problem_id VARCHAR(36) NOT NULL,
    acknowledged_by VARCHAR(100) NOT NULL,
    acknowledged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comment TEXT,
    FOREIGN KEY (problem_id) REFERENCES service_problems(id) ON DELETE CASCADE,
    INDEX idx_problem_id (problem_id)
);

-- Create problem_groups table
CREATE TABLE IF NOT EXISTS problem_groups (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    INDEX idx_name (name)
);

-- Create problem_group_members table (for many-to-many relationship)
CREATE TABLE IF NOT EXISTS problem_group_members (
    group_id VARCHAR(36) NOT NULL,
    problem_id VARCHAR(36) NOT NULL,
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    added_by VARCHAR(100),
    PRIMARY KEY (group_id, problem_id),
    FOREIGN KEY (group_id) REFERENCES problem_groups(id) ON DELETE CASCADE,
    FOREIGN KEY (problem_id) REFERENCES service_problems(id) ON DELETE CASCADE
);

-- Create problem_history table for tracking changes
CREATE TABLE IF NOT EXISTS problem_history (
    id VARCHAR(36) PRIMARY KEY,
    problem_id VARCHAR(36) NOT NULL,
    field_name VARCHAR(100) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100),
    FOREIGN KEY (problem_id) REFERENCES service_problems(id) ON DELETE CASCADE,
    INDEX idx_problem_id (problem_id),
    INDEX idx_changed_at (changed_at)
);

-- Create event_subscriptions table
CREATE TABLE IF NOT EXISTS event_subscriptions (
    id VARCHAR(36) PRIMARY KEY,
    callback_url VARCHAR(255) NOT NULL,
    event_types JSON NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    INDEX idx_status (status)
);

-- Add some sample data
INSERT INTO service_problems (id, title, description, priority, severity, category, status, created_by)
VALUES 
    ('1', 'Network Connectivity Issue', 'Users reporting intermittent network connectivity', 'HIGH', 'HIGH', 'NETWORK', 'NEW', 'system'),
    ('2', 'Database Performance', 'Slow query response times', 'MEDIUM', 'MEDIUM', 'DATABASE', 'NEW', 'system');

-- Create indexes for better performance
CREATE INDEX idx_problem_status_priority ON service_problems(status, priority);
CREATE INDEX idx_problem_created_by ON service_problems(created_by);
CREATE INDEX idx_problem_assigned_to ON service_problems(assigned_to); 