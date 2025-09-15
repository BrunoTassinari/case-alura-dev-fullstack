CREATE TABLE Registration (
    id                   bigint(20)    NOT NULL AUTO_INCREMENT,
    studentId            bigint(20)    NOT NULL,
    courseId             bigint(20)    NOT NULL,
    registrationDate     datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (studentId)  REFERENCES User(id),
    FOREIGN KEY (courseId)    REFERENCES Course(id),
    CONSTRAINT UC_Student_Course UNIQUE (studentId, courseId)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;