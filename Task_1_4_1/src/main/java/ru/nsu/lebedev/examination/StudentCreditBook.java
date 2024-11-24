package ru.nsu.lebedev.examination;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a credit book with additional logic for scholarships and diplomas.
 */
public class StudentCreditBook extends CreditBook {
    private static final double MIN_AVG_FOR_RED_DIPLOMA = 4.75;
    private static final double MIN_SEM_FOR_INC_SCHOLARSHIP = 3;

    /**
     * Constructs a StudentCreditBook object.
     *
     * @param student the student associated with this credit book
     */
    public StudentCreditBook(Student student) {
        super(student);
    }

    /**
     * Checks if the student qualifies for an increased scholarship.
     *
     * @return true if eligible, false otherwise
     */
    public boolean canObtainIncreasedScholarship() {
        int currentSemester = getStudent().getSemesterNumber();
        if (currentSemester < MIN_SEM_FOR_INC_SCHOLARSHIP) {
            return false;
        }
        var lastTwoSemestersGrades = getGrades().stream()
                .filter(grade -> grade.getSemester() == currentSemester || grade.getSemester() == currentSemester - 1)
                .toList();
        boolean hasUnsatisfactoryExams = lastTwoSemestersGrades.stream()
                .filter(grade -> grade.getDisciplineType() == DisciplineType.EXAM)
                .anyMatch(grade -> grade.getValue() == Grade.MIN_VALUE);
        if (hasUnsatisfactoryExams) {
            return false;
        }
        return true;
    }

    /**
     * Get last academic grades set.
     *
     * @return set of last Academic Grades set
     */
    private Set<Grade> getLastAcademicGrades() {
        HashMap<AcademicDiscipline, Grade> lastGrades = new HashMap<>();
        for (var grade : getGrades()) {
            Grade prevGrade = lastGrades.putIfAbsent(grade.getAcademicDiscipline(), grade);
            if (prevGrade != null && grade.isAwardedLaterThan(prevGrade)) {
                lastGrades.replace(grade.getAcademicDiscipline(), prevGrade, grade);
            }
        }
        return new HashSet<>(lastGrades.values());
    }

    /**
     * Checks if the student qualifies for a red diploma.
     *
     * @return true if eligible, false otherwise
     */
    public boolean canObtainRedDiploma() {
        var lastGrades = getLastAcademicGrades();
        double avg = 0;
        for (var grade : lastGrades) {
            if (grade.getValue() <= 3) {
                return false;
            }
            if (grade.getAcademicDiscipline().equals(AcademicDiscipline.FINAL_THESIS)
                    && grade.getValue() < Grade.MAX_VALUE) {
                return false;
            }
            avg += grade.getValue();
        }
        avg += (AcademicDiscipline.values().length - lastGrades.size()) * 5;
        avg /= AcademicDiscipline.values().length;
        return avg >= MIN_AVG_FOR_RED_DIPLOMA;
    }
}
