CREATE TABLE schedule(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    group_id uuid REFERENCES groups(id),
    room_id uuid REFERENCES rooms(id),
    teacher_id uuid REFERENCES teachers(id),
    discipline_id uuid REFERENCES disciplines(id),
    date date,
    start_time time,
    end_time time
);