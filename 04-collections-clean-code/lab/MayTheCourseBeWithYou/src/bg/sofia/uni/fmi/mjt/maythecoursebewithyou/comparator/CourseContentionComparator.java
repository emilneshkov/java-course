package bg.sofia.uni.fmi.mjt.maythecoursebewithyou.comparator;

import bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity.Course;

import java.util.Comparator;
import java.util.Map;

public class CourseContentionComparator implements Comparator<Course> {

    private final Map<String, Integer> waitlistedCounts;
    private final Map<String, Integer> seatedCounts;

    public CourseContentionComparator(Map<String, Integer> waitlistedCounts, Map<String, Integer> seatedCounts) {
        this.waitlistedCounts = waitlistedCounts;
        this.seatedCounts = seatedCounts;
    }

    @Override
    public int compare(Course a, Course b) {
        int result = Integer.compare(waitlistedCounts.get(b.code()), waitlistedCounts.get(a.code()));

        if (result != 0) {
            return result;
        }

        result = Integer.compare(seatedCounts.get(b.code()), seatedCounts.get(a.code()));

        if (result != 0) {
            return result;
        }

        result = a.title().compareTo(b.title());

        if (result != 0) {
            return result;
        }

        return a.code().compareTo(b.code());
    }
}