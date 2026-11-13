package bg.sofia.uni.fmi.mjt.maythecoursebewithyou.impl;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.StatisticsDashboard;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.comparator.CourseContentionComparator;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.comparator.StudentLoadComparator;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Course;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Student;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.CourseNotEnlistedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class StatisticsDashboardImpl implements StatisticsDashboard {
    private final Map<String, CourseEnrollment> courses;
    private final Map<String, Student> students;

    public StatisticsDashboardImpl(Map<String, CourseEnrollment> courses, Map<String, Student> students) {
        this.courses = courses;
        this.students = students;
    }

    @Override
    public Set<Course> getMostContestedCourses() {
        Map<String, Integer> waitlistedCounts = new HashMap<>();
        Map<String, Integer> seatedCounts = new HashMap<>();

        for (CourseEnrollment enrollment : courses.values()) {
            String code = enrollment.course().code();

            waitlistedCounts.put(code, enrollment.waitlistCount());
            seatedCounts.put(code, enrollment.seatedCount());
        }

        Set<Course> result = new TreeSet<>(new CourseContentionComparator(waitlistedCounts, seatedCounts));

        for (CourseEnrollment enrollment : courses.values()) {
            result.add(enrollment.course());
        }

        return Collections.unmodifiableSet(result);
    }

    @Override
    public List<Student> getBusiestStudents() {
        Map<String, Integer> courseCounts = new HashMap<>();

        for (Student student : students.values()) {
            courseCounts.put(student.facultyNumber(), 0);
        }

        for (CourseEnrollment enrollment : courses.values()) {
            for (String facultyNumber : enrollment.seatedIds()) {
                courseCounts.put(facultyNumber, courseCounts.get(facultyNumber) + 1);
            }
        }

        List<Student> result = new ArrayList<>(students.values());

        result.sort(new StudentLoadComparator(courseCounts));

        return Collections.unmodifiableList(result);
    }

    @Override
    public Map<String, Integer> countStudentsPerCourse() {
        Map<String, Integer> result = new HashMap<>();

        for (Map.Entry<String, CourseEnrollment> entry : courses.entrySet()) {
            result.put(entry.getKey(), entry.getValue().seatedCount());
        }

        return Collections.unmodifiableMap(result);
    }

    @Override
    public Set<Student> getEnrolledStudents(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Invalid course code");
        }

        CourseEnrollment enrollment = courses.get(courseCode);

        if (enrollment == null) {
            throw new CourseNotEnlistedException("Course does not exist");
        }

        Set<Student> result = new HashSet<>();

        for (String facultyNumber : enrollment.seatedIds()) {
            result.add(students.get(facultyNumber));
        }

        return Collections.unmodifiableSet(result);
    }

    @Override
    public List<Student> getWaitlist(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Invalid course code");
        }

        CourseEnrollment enrollment = courses.get(courseCode);

        if (enrollment == null) {
            throw new CourseNotEnlistedException("Course does not exist");
        }

        List<Student> result = new ArrayList<>();

        for (String facultyNumber : enrollment.waitlistIds()) {
            result.add(students.get(facultyNumber));
        }

        return Collections.unmodifiableList(result);
    }
}
