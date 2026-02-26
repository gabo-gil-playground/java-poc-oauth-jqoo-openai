CREATE TABLE IF NOT EXISTS blog_request (
	blog_request_id INTEGER PRIMARY KEY,
	create_user text NOT NULL,
	summarize text NULL,
	create_ts timestamptz NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	update_ts timestamptz NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);

CREATE TABLE IF NOT EXISTS blog_request_content (
	blog_request_content_id INTEGER PRIMARY KEY,
	blog_request_id INTEGER NOT NULL,
	blog_url text NOT NULL,
	blog_text text NOT NULL,
	create_ts timestamptz NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	update_ts timestamptz NOT NULL DEFAULT (CURRENT_TIMESTAMP),
	CONSTRAINT blog_request_content_blog_request_id_fkey FOREIGN KEY (blog_request_id) REFERENCES blog_request(blog_request_id)
);
