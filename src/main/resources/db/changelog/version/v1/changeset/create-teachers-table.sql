CREATE TABLE teachers(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name text,
    last_name text
);