package bg.sofia.uni.fmi.mjt.maythecoursebewithyou.entity;

public record Student(String facultyNumber, String name) {
    public Student {
        if (facultyNumber == null || facultyNumber.isBlank()) {
            throw new IllegalArgumentException("Invalid faculty number");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid student name");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Student other)) {
            return false;
        }

        return facultyNumber.equals(other.facultyNumber);
    }

    @Override
    public int hashCode() {
        return facultyNumber.hashCode();
    }
}