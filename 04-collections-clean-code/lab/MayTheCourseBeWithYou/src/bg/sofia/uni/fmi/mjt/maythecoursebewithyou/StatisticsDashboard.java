package bg.sofia.uni.fmi.mjt.maythecoursebewithyou;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Course;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Student;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.CourseNotEnlistedException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StatisticsDashboard {

    /**
     * Ranks the enlisted courses by contention, i.e. how over-subscribed they are. The primary
     * rank key is the number of waitlisted students, in descending order; ties are broken first by
     * the number of enrolled students (descending), then by course title in ascending lexicographic
     * order.
     *
     * @return an unmodifiable Set of all enlisted courses in the described order
     *         (empty if there are no courses)
     */
    Set<Course> getMostContestedCourses();

    /**
     * Ranks the registered students by course load. The rank key is the number of courses the
     * student currently holds a seat in, in descending order; ties are broken by faculty number in
     * ascending lexicographic order. Waitlisted-only placements do not count towards the load.
     *
     * @return an unmodifiable List of all registered students in the described order
     *         (empty if there are no students)
     */
    List<Student> getBusiestStudents();

    /**
     * Counts the enrolled students of every enlisted course.
     *
     * @return an unmodifiable Map whose keys are the codes of the enlisted courses, and whose values
     *         are integers representing the count of students enrolled (seated) in the given course.
     */
    Map<String, Integer> countStudentsPerCourse();

    /**
     * Returns the students currently enrolled in the course with the given code.
     *
     * @param courseCode the code of the course
     * @return an unmodifiable set of the students currently enrolled in the course
     *         (empty if none); iteration order is unspecified
     * @throws CourseNotEnlistedException  if no course with the given code is enlisted
     * @throws IllegalArgumentException if courseCode is null or blank
     */
    Set<Student> getEnrolledStudents(String courseCode);

    /**
     * Returns the students currently on the waitlist for the course
     * with the given code in the order they will be seated.
     *
     * @param courseCode the code of the course
     * @return an unmodifiable list of the students on the course's waitlist, in the order they
     *         will be seated; empty if none
     * @throws CourseNotEnlistedException  if no course with the given code is enlisted
     * @throws IllegalArgumentException if courseCode is null or blank
     */
    List<Student> getWaitlist(String courseCode);

}