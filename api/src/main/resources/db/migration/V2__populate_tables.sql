INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (1, 'admin', 'admin@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'ADMIN', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (101, 'Admin', 1, 'Administration', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (2, 'manager1', 'manager1@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'MANAGER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (102, 'Manager One', 2, 'Management', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (3, 'manager2', 'manager2@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'MANAGER', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (103, 'Manager Two', 3, 'Management', NULL, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (4, 'collab1', 'collab1@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (104, 'Collaborator One', 4, 'Development', 102, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (5, 'collab2', 'collab2@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (105, 'Collaborator Two', 5, 'Development', 102, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (6, 'collab3', 'collab3@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (106, 'Collaborator Three', 6, 'Development', 102, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (7, 'collab4', 'collab4@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (107, 'Collaborator Four', 7, 'Development', 102, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (8, 'collab5', 'collab5@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (108, 'Collaborator Five', 8, 'Development', 103, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (9, 'collab6', 'collab6@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (109, 'Collaborator Six', 9, 'Development', 103, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (id, username, email, password, role, active, created_at, updated_at)
VALUES (10, 'collab7', 'collab7@example.com', '$2a$10$kFgHcnfIn90IcfX/fYR2vOV1rG4Io7JSXk/003xaU8lNNo82d0iyi', 'COLLABORATOR', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO collaborators (id, name, user_id, department, manager_id, active, created_at, updated_at)
VALUES (110, 'Collaborator Seven', 10, 'Development', 103, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO vacations (id, collaborator_id, start_date, end_date, vacation_status, created_at, updated_at)
VALUES (1, 104, '2025-10-01', '2025-10-05', 'PENDING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);