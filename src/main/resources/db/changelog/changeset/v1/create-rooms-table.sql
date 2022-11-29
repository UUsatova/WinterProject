CREATE TABLE rooms(
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    number integer,
    address text
);