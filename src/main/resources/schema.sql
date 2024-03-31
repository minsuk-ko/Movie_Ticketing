-- -----------------------------------------------------
-- Table `movie`.`Theater`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS theater (
theater_id     INT         NOT NULL,
theater_name   VARCHAR(5)  NOT NULL,
PRIMARY KEY (`theater_id`));

-- -----------------------------------------------------
-- Table `movie`.`seat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS seat (
seat_id     INT         NOT NULL,
seat_row    VARCHAR(10) NOT NULL,
seat_col    INT         NOT NULL,
theater_id  INT         NOT NULL,
isSelected  BOOLEAN,
PRIMARY KEY (seat_id),
INDEX fk_seat_theater1_idx (theater_id ASC) VISIBLE,
CONSTRAINT fk_seat_theater1
FOREIGN KEY (theater_id)
REFERENCES theater (theater_id));


-- -----------------------------------------------------
-- Table `movie`.`movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS movie (
movie_id             INT         NOT NULL,
movie_title          VARCHAR(45) NOT NULL,
movie_story          VARCHAR(500)NOT NULL,
movie_director       VARCHAR(10) NOT NULL,
movie_actor          VARCHAR(100)NOT NULL,
movie_rating         VARCHAR(5)  NOT NULL,
movie_poster_url     TEXT        NOT NULL,
movie_openDate       DATE        NOT NULL,
movie_runtime        VARCHAR(15) NOT NULL,
movie_state          BOOLEAN,
PRIMARY KEY (movie_id),
UNIQUE KEY movie_title (movie_title));


-- -----------------------------------------------------
-- Table `movie`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS schedule (
schedule_id         INT         NOT NULL,
schedule_start      VARCHAR(15) NOT NULL,
schedule_end        VARCHAR(15) NOT NULL,
schedule_date       VARCHAR(20) NOT NULL,
movie_id            INT         NOT NULL,
theater_id          INT         NOT NULL,
PRIMARY KEY (schedule_id),
INDEX fk_schedule_movie_idx (movie_id ASC) VISIBLE,
INDEX fk_schedule_theater1_idx (theater_id ASC) VISIBLE,
CONSTRAINT fk_schedule_movie
FOREIGN KEY (movie_id)
REFERENCES movie (movie_id),
CONSTRAINT fk_schedule_theater1
FOREIGN KEY (theater_id)
REFERENCES theater (theater_id));


-- -----------------------------------------------------
-- Table `movie`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS member (
member_id        INT         NOT NULL,
member_password  VARCHAR(25) NOT NULL,
member_email     VARCHAR(25) NOT NULL,
member_name      VARCHAR(10) NOT NULL,
member_age       INT         NOT NULL,
PRIMARY KEY (member_id),
UNIQUE KEY member_email (member_email));


-- -----------------------------------------------------
-- Table `movie`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS reservation (
reservation_id           INT         NOT NULL,
reservation_date         VARCHAR(10) NOT NULL,
reservation_price        INT         NOT NULL,
reservation_ticket_num   INT         NOT NULL,
member_id                INT         NOT NULL,
PRIMARY KEY (reservation_id),
INDEX fk_reservation_member1_idx (member_id ASC) VISIBLE,
CONSTRAINT fk_reservation_member1
FOREIGN KEY (member_id)
REFERENCES member (member_id));


-- -----------------------------------------------------
-- Table `movie`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ticket (
ticket_id           INT         NOT NULL,
ticket_price        INT         NOT NULL,
schedule_id         INT         NOT NULL,
seat_id             INT         NOT NULL,
reservation_id      INT         NOT NULL,
PRIMARY KEY (ticket_id),
INDEX fk_ticket_schedule1_idx (schedule_id ASC) VISIBLE,
INDEX fk_ticket_seat1_idx (seat_id ASC) VISIBLE,
INDEX fk_ticket_reservation1_idx (reservation_id ASC) VISIBLE,
CONSTRAINT fk_ticket_schedule1
FOREIGN KEY (schedule_id)
REFERENCES schedule (schedule_id),
CONSTRAINT fk_ticket_seat1
FOREIGN KEY (seat_id)
REFERENCES seat (seat_id),
CONSTRAINT fk_ticket_reservation1
FOREIGN KEY (reservation_id)
REFERENCES reservation (reservation_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION)
ENGINE = InnoDB;
