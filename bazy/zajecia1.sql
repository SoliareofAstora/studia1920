create table Person(
  PersonID int primary key ,
  LastName varchar(50),
  FirstName varchar(50),
  HireDate date,
  EnrollmentDate date
);

create table Department(
    DepartmentID int primary key ,
    Name varchar(50),
    Budget int,
    StartDate date,
    Administrator varchar(50)
);

create table Course(
    CourseID int primary key ,
    Title varchar(50),
    Credits int,
    DepartmentID int references Department(DepartmentID)
);

create table StudentsGrade(
    EnrollmentID int primary key,
    CourseID int references Course(CourseID),
    StudentID int references Person(PersonID),
    Grade int
);

create table OfficeAssignment(
    InstructorID int primary key,
    Location varchar(50),
    TimeStamp date
);

create table CourseInstructor(
    CourseID int,
    PersonsID int
);

create table OnlineCourse(
    CourseID int,
    URL varchar(50)
);

create table OnSiteCourse(
    CourseID int,
    Location varchar(50),
    Days int,
    Time time
);

alter table Person add BirthDate date;
alter table Person add unique (FirstName, LastName);
alter table Person add Age decimal;
alter table Person alter column Age int;
alter table Person alter EnrollmentDate set default now();

drop table OfficeAssignment;

alter table StudentsGrade add check ( Grade >= 2 and Grade <=5);
alter table StudentsGrade alter Grade set default 0;

alter table Department drop column Administrator;
