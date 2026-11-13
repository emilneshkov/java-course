package bg.sofia.uni.fmi.mjt.maythecoursebewithyou.comparator;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Student;

import java.util.Comparator;
import java.util.Map;

public class StudentLoadComparator implements Comparator<Student> {

    private final Map<String, Integer> courseCounts;

    public StudentLoadComparator(Map<String, Integer> courseCounts) {
        this.courseCounts = courseCounts;
    }

    @Override
    public int compare(Student a, Student b) {
        int result = Integer.compare(courseCounts.get(b.facultyNumber()), courseCounts.get(a.facultyNumber()));

        if (result != 0) {
            return result;
        }

        return a.facultyNumber().compareTo(b.facultyNumber());
    }
}