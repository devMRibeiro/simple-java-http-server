create table task (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  title varchar(255) not null,
  description varchar(255),
  status varchar(20),
  priority varchar(10),
  due_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)