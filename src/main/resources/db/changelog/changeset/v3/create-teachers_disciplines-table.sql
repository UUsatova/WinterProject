CREATE TABLE teachers_disciplines(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    teacher_id uuid REFERENCES teachers(id)
);