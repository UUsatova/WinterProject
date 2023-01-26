--

CREATE TABLE users(
            id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
            role text,
            login text,
            password text,
            student_id uuid REFERENCES students(id),
            teacher_id uuid REFERENCES teachers(id)
);