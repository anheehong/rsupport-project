INSERT INTO RS_USER (username, email, created_at)
VALUES ('user1','user1@rsupport.com', CURRENT_DATE());

INSERT INTO RS_USER (username, email, created_at)
VALUES ('user2','user2@rsupport.com', CURRENT_DATE());

INSERT INTO RS_USER (username, email, created_at)
VALUES ('user3','user3@rsupport.com', CURRENT_DATE());

ALTER TABLE RS_NOTICE ALTER COLUMN ID RESTART WITH 100;

INSERT INTO RS_NOTICE (title, contents, "from", "to", user_id, created_at, views)
VALUES ('게시글 1 - 공지기간 진행중 aa', '게시글 1 내용', CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 1, CURRENT_DATE(), 1);

INSERT INTO RS_NOTICE (title, contents, "from", "to", user_id, created_at, views)
VALUES ('게시글 2 - 공지기간 진행중 b', '게시글 2 내용', DATEADD('DAY', -3, CURRENT_DATE), DATEADD('DAY', 4, CURRENT_DATE), 1, CURRENT_DATE(), 1);

INSERT INTO RS_NOTICE (title, contents, "from", "to", user_id, created_at, views)
VALUES ('게시글 3 - 공지기간 지남', '게시글 3 내용 c', DATEADD('DAY', -7, CURRENT_DATE), DATEADD('DAY', -1, CURRENT_DATE), 1, CURRENT_DATE(), 1);

INSERT INTO RS_NOTICE (title, contents, "from", "to", user_id, created_at, views)
VALUES ('게시글 4 - 공지기간 진행중', '게시글 4 내용', DATEADD('DAY', -2, CURRENT_DATE), DATEADD('DAY', 3, CURRENT_DATE), 1, CURRENT_DATE(), 1);

INSERT INTO RS_NOTICE (title, contents, "from", "to", user_id, created_at, views)
VALUES ('게시글 5- 공지기간 예정', '게시글 5 내용', DATEADD('DAY', 3, CURRENT_DATE), DATEADD('DAY', 7, CURRENT_DATE), 1, CURRENT_DATE(), 1);