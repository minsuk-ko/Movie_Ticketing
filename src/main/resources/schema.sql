-- -----------------------------------------------------
-- Table `movie`.`Theater`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS theater (
id     INT         NOT NULL,
name   VARCHAR(5)  NOT NULL,
PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `movie`.`seat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS seat (
id     INT         NOT NULL,
row    VARCHAR(10) NOT NULL,
col    INT         NOT NULL,
theater_id  INT         NOT NULL,
isSelected  BOOLEAN,
PRIMARY KEY (id),
INDEX fk_seat_theater1_idx (theater_id ASC) VISIBLE,
CONSTRAINT fk_seat_theater1
FOREIGN KEY (theater_id)
REFERENCES theater (id));


-- -----------------------------------------------------
-- Table `movie`.`movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS movie (
id             INT         NOT NULL,
title          VARCHAR(45) NOT NULL,
story          VARCHAR(500)NOT NULL,
director       VARCHAR(10) NOT NULL,
actor          VARCHAR(100)NOT NULL,
rating         VARCHAR(5)  NOT NULL,
poster_url     TEXT        NOT NULL,
openDate       DATE        NOT NULL,
runtime        VARCHAR(15) NOT NULL,
state          BOOLEAN,
PRIMARY KEY (id),
UNIQUE KEY movie_title (title));


-- -----------------------------------------------------
-- Table `movie`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS schedule (
id         INT         NOT NULL,
start      VARCHAR(15) NOT NULL,
end        VARCHAR(15) NOT NULL,
date       VARCHAR(20) NOT NULL,
movie_id            INT         NOT NULL,
theater_id          INT         NOT NULL,
PRIMARY KEY (id),
INDEX fk_schedule_movie_idx (movie_id ASC) VISIBLE,
INDEX fk_schedule_theater1_idx (theater_id ASC) VISIBLE,
CONSTRAINT fk_schedule_movie
FOREIGN KEY (movie_id)
REFERENCES movie (id),
CONSTRAINT fk_schedule_theater1
FOREIGN KEY (theater_id)
REFERENCES theater (id));


-- -----------------------------------------------------
-- Table `movie`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS member (
id        INT         NOT NULL,
password  VARCHAR(25) NOT NULL,
email     VARCHAR(25) NOT NULL,
name      VARCHAR(10) NOT NULL,
age       INT         NOT NULL,
PRIMARY KEY (id),
UNIQUE KEY member_email (email));


-- -----------------------------------------------------
-- Table `movie`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS reservation (
id           INT         NOT NULL,
date         VARCHAR(10) NOT NULL,
price        INT         NOT NULL,
ticket_num   INT         NOT NULL,
member_id                INT         NOT NULL,
PRIMARY KEY (id),
INDEX fk_reservation_member1_idx (member_id ASC) VISIBLE,
CONSTRAINT fk_reservation_member1
FOREIGN KEY (member_id)
REFERENCES member (id));


-- -----------------------------------------------------
-- Table `movie`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ticket (
id           INT         NOT NULL,
price        INT         NOT NULL,
schedule_id         INT         NOT NULL,
seat_id             INT         NOT NULL,
reservation_id      INT         NOT NULL,
PRIMARY KEY (id),
INDEX fk_ticket_schedule1_idx (schedule_id ASC) VISIBLE,
INDEX fk_ticket_seat1_idx (seat_id ASC) VISIBLE,
INDEX fk_ticket_reservation1_idx (reservation_id ASC) VISIBLE,
CONSTRAINT fk_ticket_schedule1
FOREIGN KEY (schedule_id)
REFERENCES schedule (id),
CONSTRAINT fk_ticket_seat1
FOREIGN KEY (seat_id)
REFERENCES seat (id),
CONSTRAINT fk_ticket_reservation1
FOREIGN KEY (reservation_id)
REFERENCES reservation (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION)
ENGINE = InnoDB;
