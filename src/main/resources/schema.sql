-- -----------------------------------------------------
-- Table `movie`.`Theater`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Theater (
Theater_id     INT         NOT NULL,
Theater_name   VARCHAR(5)  NOT NULL,
PRIMARY KEY (`Theater_id`));

-- -----------------------------------------------------
-- Table `movie`.`Seat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Seat (
Seat_id     INT         NOT NULL,
Seat_row    VARCHAR(10) NOT NULL,
Seat_col    INT         NOT NULL,
Theater_id  INT         NOT NULL,
IsSelected  BOOLEAN,
PRIMARY KEY (Seat_id),
INDEX fk_Seat_Theater1_idx (Theater_id ASC) VISIBLE,
CONSTRAINT fk_Seat_Theater1
FOREIGN KEY (Theater_id)
REFERENCES Theater (Theater_id));


-- -----------------------------------------------------
-- Table `movie`.`Movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Movie (
Movie_id             INT         NOT NULL,
Movie_title          VARCHAR(45) NOT NULL,
Movie_story          VARCHAR(500)NOT NULL,
Movie_director       VARCHAR(10) NOT NULL,
Movie_actor          VARCHAR(100)NOT NULL,
Movie_rating         VARCHAR(5)  NOT NULL,
Movie_poster_url     TEXT        NOT NULL,
Movie_openDate       DATE        NOT NULL,
Movie_runtime        VARCHAR(15) NOT NULL,
Movie_state          BOOLEAN,
PRIMARY KEY (Movie_id),
UNIQUE KEY Movie_title (Movie_title));


-- -----------------------------------------------------
-- Table `movie`.`Schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Schedule (
Schedule_id         INT         NOT NULL,
Schedule_start      VARCHAR(15) NOT NULL,
Schedule_end        VARCHAR(15) NOT NULL,
Schedule_date       VARCHAR(20) NOT NULL,
Movie_id            INT         NOT NULL,
Theater_id          INT         NOT NULL,
PRIMARY KEY (Schedule_id),
INDEX fk_Schedule_Movie_idx (Movie_id ASC) VISIBLE,
INDEX fk_Schedule_Theater1_idx (Theater_id ASC) VISIBLE,
CONSTRAINT fk_Schedule_Movie
FOREIGN KEY (Movie_id)
REFERENCES Movie (Movie_id),
CONSTRAINT fk_Schedule_Theater1
FOREIGN KEY (Theater_id)
REFERENCES Theater (Theater_id));


-- -----------------------------------------------------
-- Table `movie`.`Member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Member (
Member_id        INT         NOT NULL,
Member_password  VARCHAR(25) NOT NULL,
Member_email     VARCHAR(25) NOT NULL,
Member_name      VARCHAR(10) NOT NULL,
Member_age       INT         NOT NULL,
PRIMARY KEY (Member_id),
UNIQUE KEY Member_email (Member_email));


-- -----------------------------------------------------
-- Table `movie`.`Reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Reservation (
Reservation_id           INT         NOT NULL,
Reservation_date         DATE        NOT NULL,
Reservation_price        VARCHAR(10) NOT NULL,
Reservation_ticket_num   VARCHAR(20) NOT NULL,
Member_id                INT         NOT NULL,
PRIMARY KEY (Reservation_id),
INDEX fk_Reservation_Member1_idx (Member_id ASC) VISIBLE,
CONSTRAINT fk_Reservation_Member1
FOREIGN KEY (Member_id)
REFERENCES Member (Member_id));


-- -----------------------------------------------------
-- Table `movie`.`Ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS Ticket (
Ticket_id           INT         NOT NULL,
Ticket_price        INT         NOT NULL,
Schedule_id         INT         NOT NULL,
Seat_id             INT         NOT NULL,
Reservation_id      INT         NOT NULL,
PRIMARY KEY (Ticket_id),
INDEX fk_Ticket_Schedule1_idx (Schedule_id ASC) VISIBLE,
INDEX fk_Ticket_Seat1_idx (Seat_id ASC) VISIBLE,
INDEX fk_Ticket_Reservation1_idx (Reservation_id ASC) VISIBLE,
CONSTRAINT fk_Ticket_Schedule1
FOREIGN KEY (Schedule_id)
REFERENCES Schedule (Schedule_id),
CONSTRAINT fk_Ticket_Seat1
FOREIGN KEY (Seat_id)
REFERENCES Seat (Seat_id),
CONSTRAINT fk_Ticket_Reservation1
FOREIGN KEY (Reservation_id)
REFERENCES Reservation (Reservation_id));
