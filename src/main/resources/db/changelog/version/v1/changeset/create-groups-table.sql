CREATE TABLE groups(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    number integer,
    number_of_students smallint,
    year smallint
);