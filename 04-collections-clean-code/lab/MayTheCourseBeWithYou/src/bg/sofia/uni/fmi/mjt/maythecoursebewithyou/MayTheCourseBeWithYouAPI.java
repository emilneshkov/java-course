package bg.sofia.uni.fmi.mjt.maythecoursebewithyou;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Course;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Student;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.AlreadyEnrolledException;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.CourseNotEnlistedException;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.CourseAlreadyEnrolledException;

public interface MayTheCourseBeWithYouAPI {

    /**
     * Enlists (opens) a course for enrollment.
     *
     * @param course the course to enlist
     * @throws IllegalArgumentException if course is null
     * @throws CourseAlreadyEnrolledException if a course with the same code is already enlisted
     */
    void enlistCourse(Course course);

    /**
     * Delists (removes) the course with the given code, together with its enrollments and its
     * waitlist.
     *
     * @param courseCode the code of the course
     * @return the delisted course
     * @throws IllegalArgumentException if courseCode is null or blank
     * @throws CourseNotEnlistedException  if no course with the given code is enlisted
     */
    Course delistCourse(String courseCode);

    /**
     * Adds the student to the enrollment for the course with the given code. If the student is not
     * yet known to the system, enrolling registers them.
     *
     * @param student    the student who wants to enroll
     * @param courseCode the code of the course
     * @return true if the student got a seat, false if the student was put on the waitlist
     * @throws IllegalArgumentException if student is null, or courseCode is null or blank
     * @throws AlreadyEnrolledException if the student is already enrolled in or already waitlisted
     *                                  for this course
     * @throws CourseNotEnlistedException  if no course with the given code is enlisted
     */
    boolean enroll(Student student, String courseCode);

    /**
     * Removes the student from the enrollment for the course with the given code. If the student
     * is neither enrolled nor waitlisted for the course, the system is left unchanged.
     *
     * @param student    the student who drops the course
     * @param courseCode the code of the course
     * @return true if the student got dropped from the course, false if the student was neither enrolled
     * nor waitlisted for the course
     * @throws IllegalArgumentException if student is null, or courseCode is null or blank
     * @throws CourseNotEnlistedException  if no course with the given code is enlisted
     */
    boolean drop(Student student, String courseCode);

    /**
     * Provides access to the statistics dashboard. The dashboard reflects the current state of the
     * system in terms of courses and students, and sees any further changes to the state in real time.
     *
     * @return the statistics dashboard for the system
     */
    StatisticsDashboard getStatisticsDashboard();

}