CREATE TABLE users (
                       id BIGINT NOT NULL AUTO_INCREMENT,
                       username VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       PRIMARY KEY (id),
                       UNIQUE KEY uk_users_username (username),
                       UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB;

CREATE TABLE collaborators (
                               id BIGINT NOT NULL AUTO_INCREMENT,
                               name VARCHAR(255) NOT NULL,
                               user_id BIGINT NOT NULL,
                               department VARCHAR(255),
                               manager_id BIGINT DEFAULT NULL,
                               active BOOLEAN DEFAULT TRUE,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               PRIMARY KEY (id),
                               UNIQUE KEY uk_collaborators_user (user_id),
                               CONSTRAINT fk_collaborators_user FOREIGN KEY (user_id) REFERENCES users(id),
                               CONSTRAINT fk_collaborators_manager FOREIGN KEY (manager_id) REFERENCES collaborators(id)
) ENGINE=InnoDB;

CREATE TABLE vacations (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           collaborator_id BIGINT NOT NULL,
                           start_date DATE NOT NULL,
                           end_date DATE NOT NULL,
                           vacation_status VARCHAR(50) NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_timeoff_collaborator FOREIGN KEY (collaborator_id) REFERENCES collaborators(id)
) ENGINE=InnoDB;