CREATE TABLE job_boards (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  rating VARCHAR(256) NOT NULL,
  root_domain VARCHAR(64) DEFAULT NULL,
  logo_file VARCHAR(256) DEFAULT NULL,
  description TEXT DEFAULT NULL
);

CREATE TABLE job_opportunities (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  job_title TEXT NOT NULL,
  company_name VARCHAR(256) NOT NULL,
  job_url TEXT DEFAULT NULL
);

CREATE TABLE matched_job_opportunities (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  job_title TEXT NOT NULL,
  company_name VARCHAR(256) NOT NULL,
  job_url TEXT DEFAULT NULL,
  job_source VARCHAR(256) NOT NULL
);
