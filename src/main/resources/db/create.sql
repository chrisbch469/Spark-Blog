
CREATE TABLE IF NOT EXISTS posts (
  post_id INTEGER PRIMARY KEY auto_increment,
  title VARCHAR,
  content VARCHAR,
  creationdate DATETIME
);

CREATE TABLE IF NOT EXISTS comments (
  comment_id INTEGER PRIMARY KEY auto_increment,
  post_id INTEGER,
  author VARCHAR,
  content VARCHAR,
  submissiondate DATETIME,
  FOREIGN KEY(post_id) REFERENCES PUBLIC.posts(post_id) ON DELETE CASCADE
);

