CREATE TABLE students(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_id uuid REFERENCES groups(id),
    first_name text,
    second_name text
);