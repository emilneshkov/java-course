package bg.sofia.uni.fmi.mjt.maythecoursebewithyou.impl;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Course;
import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.exception.AlreadyEnrolledException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

class CourseEnrollment {
    private final Course course;
    private final Set<String> seated;
    private final Set<String> waitlist;

    /**
     * Creates an empty enrollment (no seated students, empty waitlist) for the given course.
     *
     * @param course the course this enrollment tracks
     * @throws IllegalArgumentException if course is null
     */
    CourseEnrollment(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }

        this.course = course;
        this.seated = new HashSet<>();
        this.waitlist = new LinkedHashSet<>();
    }

    /**
     * Enrolls the student with the given faculty number. Seats them if the course has room (its
     * seated count is below the course's {@code capacity}), otherwise places them at the back of
     * the waitlist.
     *
     * @param facultyNumber the faculty number of the student to enroll
     * @return true if the student got a seat, false if they were placed on the waitlist
     * @throws AlreadyEnrolledException if the student is already seated or already waitlisted
     */
    boolean enroll(String facultyNumber) {
        if (facultyNumber == null || facultyNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid faculty number");
        }

        if (seated.contains(facultyNumber) || waitlist.contains(facultyNumber)) {
            throw new AlreadyEnrolledException("Student is already enrolled or waitlisted");
        }

        if (seated.size() < course.capacity()) {
            return seated.add(facultyNumber);
        } else {
            waitlist.add(facultyNumber);
            return false;
        }
    }

    /**
     * Drops the student with the given faculty number. If they held a seat, the freed seat is
     * immediately given to the student at the head of the waitlist, if any. If they were only on
     * the waitlist, they are simply removed.
     *
     * @param facultyNumber the faculty number of the student to drop
     * @return true if the student was seated or waitlisted (and thus removed), false if they were
     *         neither (the enrollment is left unchanged)
     */
    boolean drop(String facultyNumber) {
        if (facultyNumber == null || facultyNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid faculty number");
        }

        if (seated.remove(facultyNumber)) {
            Iterator<String> iterator = waitlist.iterator();

            if (iterator.hasNext()) {
                String nextStudent = iterator.next();
                iterator.remove();

                seated.add(nextStudent);
            }

            return true;
        }

        return waitlist.remove(facultyNumber);
    }

    /**
     * @return the course this enrollment tracks
     */
    Course course() {
        return course;
    }

    /**
     * @return the number of students currently holding a seat
     */
    int seatedCount() {
        return seated.size();
    }

    /**
     * @return the number of students currently on the waitlist
     */
    int waitlistCount() {
        return waitlist.size();
    }

    /**
     * @return an unmodifiable copy of the faculty numbers currently holding a seat
     */
    Set<String> seatedIds() {
        return Collections.unmodifiableSet(new HashSet<>(seated));
    }

    /**
     * @return an unmodifiable copy of the waitlisted faculty numbers, in the order they will
     *         be seated
     */
    List<String> waitlistIds() {
        return Collections.unmodifiableList(new ArrayList<>(waitlist));
    }
}