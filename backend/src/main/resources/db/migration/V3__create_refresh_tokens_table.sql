-- V3__create_refresh_tokens_table.sql
CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                token VARCHAR(255) NOT NULL,
                                expiry_date TIMESTAMP NOT NULL,
                                created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX ux_refresh_token ON refresh_tokens(token);
