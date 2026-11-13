package bg.sofia.uni.fmi.mjt.maythecoursebewithyou.impl;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.MayTheCourseBeWithYouAPI;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.StatisticsDashboard;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Course;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Student;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.CourseAlreadyEnrolledException;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.CourseNotEnlistedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MayTheCourseBeWithYou implements MayTheCourseBeWithYouAPI {
    private final Map<String, CourseEnrollment> courses;
    private final Map<String, Student> students;
    private final StatisticsDashboardImpl dashboard;

    public MayTheCourseBeWithYou() {
        this.courses = new HashMap<>();
        this.students = new HashMap<>();

        this.dashboard = new StatisticsDashboardImpl(
                Collections.unmodifiableMap(courses),
                Collections.unmodifiableMap(students)
        );
    }

    @Override
    public void enlistCourse(Course course) {
        if (course == null) {
            throw  new IllegalArgumentException("Course cannot be null");
        }

        if (courses.containsKey(course.code())) {
            throw new CourseAlreadyEnrolledException("Course already exists");
        }

        courses.put(course.code(), new CourseEnrollment(course));
    }

    @Override
    public Course delistCourse(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Invalid course code");
        }

        CourseEnrollment enrollment = courses.remove(courseCode);

        if (enrollment == null) {
            throw new CourseNotEnlistedException("Course does not exist");
        }

        return enrollment.course();
    }

    @Override
    public boolean enroll(Student student, String courseCode) {
        if (student == null || courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        CourseEnrollment enrollment = courses.get(courseCode);

        if (enrollment == null) {
            throw new CourseNotEnlistedException("Course does not exist");
        }

        students.putIfAbsent(student.facultyNumber(), student);

        return enrollment.enroll(student.facultyNumber());
    }

    @Override
    public boolean drop(Student student, String courseCode) {
        if (student == null || courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        CourseEnrollment enrollment = courses.get(courseCode);

        if (enrollment == null) {
            throw new CourseNotEnlistedException("Course does not exist");
        }

        return enrollment.drop(student.facultyNumber());
    }

    @Override
    public StatisticsDashboard getStatisticsDashboard() {
        return dashboard;
    }
}
