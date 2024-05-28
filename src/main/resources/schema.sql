-- -----------------------------------------------------
-- Table `movie`.`Theater`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS theater (
    id     INT         NOT NULL AUTO_INCREMENT,
    name   VARCHAR(5)  NOT NULL,
    PRIMARY KEY (`id`));

-- -----------------------------------------------------
-- Table `movie`.`seat`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS seat (
    id           INT    NOT NULL AUTO_INCREMENT,
    seat_number  INT    NOT NULL,
    theater_id   INT   NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (theater_id)
    REFERENCES theater (id));


-- -----------------------------------------------------
-- Table `movie`.`movie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS movie (
    id             INT         NOT NULL ,
    title          TEXT NOT NULL,
    state           BOOLEAN,
    open_date DATE NOT NULL,
     popularity DOUBLE DEFAULT 0.0,
    role       TEXT     NOT NULL,
    cast          TEXT     NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY movie_title (title));



-- -----------------------------------------------------
-- Table `movie`.`schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS schedule (
    id         INT         NOT NULL AUTO_INCREMENT,
    start      TIME        NOT NULL,
    end        TIME        NOT NULL,
    date       DATE        NOT NULL,
    movie_id            INT         NOT NULL,
    theater_id          INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (movie_id)
    REFERENCES movie (id),
    FOREIGN KEY (theater_id)
    REFERENCES theater (id));


-- -----------------------------------------------------
-- Table `movie`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS member (
    id        INT         NOT NULL AUTO_INCREMENT,
    password  VARCHAR(255) NOT NULL,
    email     VARCHAR(200) NOT NULL,
    name      VARCHAR(10) NOT NULL,
    age       INT         NOT NULL,
    role      VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY member_email (email));


-- -----------------------------------------------------
-- Table `movie`.`reservation`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS reservation (
    id           INT         NOT NULL AUTO_INCREMENT,
    date         DATE NOT NULL,
    member_id    INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (member_id)
    REFERENCES member (id));


-- -----------------------------------------------------
-- Table `movie`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ticket (
    id           INT         NOT NULL AUTO_INCREMENT,
    schedule_id         INT        NOT NULL,
    seat_id             INT         NOT NULL,
    reservation_id      INT         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (schedule_id)
    REFERENCES schedule (id),
    CONSTRAINT fk_ticket_seat1
    FOREIGN KEY (seat_id)
    REFERENCES seat (id),
    FOREIGN KEY (reservation_id)
    REFERENCES reservation (id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;
