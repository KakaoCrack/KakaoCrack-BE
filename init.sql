CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS pgcrypto;




-- users 테이블
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    kakao_id VARCHAR(255) NOT NULL,
    nickname VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- game_sessions 테이블
CREATE TABLE game_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    remaining_questions INTEGER NOT NULL,
    game_progress INTEGER NOT NULL,
    start_time TIMESTAMP,
    end_time TIMESTAMP,
    final_score INTEGER,
    ending_type VARCHAR(50)
);

-- npc_status 테이블
CREATE TABLE npc_status (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES game_sessions(id),
    npc_name VARCHAR(50) NOT NULL,
    suspicion_score INTEGER NOT NULL,
    affection_score INTEGER NOT NULL,
    is_confessed BOOLEAN,
    conversation_summary TEXT,
    chat_count INTEGER NOT NULL DEFAULT 0,
    last_updated TIMESTAMP
);

-- chat_logs 테이블

CREATE TABLE chat_logs (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES game_sessions(id),
    npc_name VARCHAR(50) NOT NULL,
    role VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- items 테이블
CREATE TABLE items (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- user_inventory 테이블
CREATE TABLE user_inventory (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES game_sessions(id),
    item_id VARCHAR(50) NOT NULL REFERENCES items(id),
    obtained_at TIMESTAMP
);

-- session_memos 테이블
CREATE TABLE session_memos (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL REFERENCES game_sessions(id),
    content TEXT NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- ✅ character_knowledge 테이블
CREATE TABLE character_knowledge (
    id BIGSERIAL PRIMARY KEY,
    character_name VARCHAR(50) NOT NULL,
    content TEXT NOT NULL,
    embedding VECTOR(1536) NOT NULL,
    category VARCHAR(50),
    trigger_type VARCHAR(50),
    trigger_value JSONB,
    is_lie BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO items (id, name, description) VALUES
('ITEM_01', '갈색 털뭉치', '현장 바닥에서 발견된 갈색 털뭉치'),
('ITEM_02', '커피 자국', '누군가 급하게 쏟은 듯한 커피 자국'),
('ITEM_03', '보안카드', '복도에 떨어져 있던 누군가의 출입 보안카드'),
('ITEM_04', '초콜릿 봉지', '반쯤 뜯겨 있는 초콜릿 봉지');

-- Indexes for character_knowledge
CREATE INDEX character_knowledge_embedding_idx ON character_knowledge USING hnsw (embedding vector_cosine_ops);