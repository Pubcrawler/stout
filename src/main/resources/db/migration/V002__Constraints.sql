ALTER TABLE users
  ADD CONSTRAINT ck_gender CHECK (gender IN ('F', 'M', 'O'));

ALTER TABLE crawl_participants
  ADD CONSTRAINT ck_status CHECK (status IN ('A', 'D', 'M'));
