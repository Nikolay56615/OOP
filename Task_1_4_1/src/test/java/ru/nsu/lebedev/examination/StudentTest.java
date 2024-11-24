package ru.nsu.lebedev.examination;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Credit book test class.
 */
public class StudentTest {
    private Student student;

    /**
     * Add standard student.
     */
    @BeforeEach
    public void setUp() {
        student = new Student("Nikolay", "Alexandrian", "Dunant");
    }

    @Test
    void testStudentChange() {
        var s2 = new Student("Nikolay", "Alexandrian", "Dunant");
        assertNotEquals(student, s2);
    }

    @Test
    void testGradeValueUpperBound() {
        assertEquals(student.getCreditBook()
                .addGrade(0, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE),
                new Grade(2, Student.SEMESTER_BASE_NUMBER,
                        AcademicDiscipline.ARTIFICIAL_INTELLIGENCE));
    }

    @Test
    void testGradeValueLowerBound() {
        assertEquals(student.getCreditBook()
                        .addGrade(100, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE),
                new Grade(5, Student.SEMESTER_BASE_NUMBER,
                        AcademicDiscipline.ARTIFICIAL_INTELLIGENCE));
    }

    @Test
    void testGradeOverridingBlocking() {
        student.getCreditBook()
                .addGrade(100, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        assertNull(student.getCreditBook().addGrade(3,
                AcademicDiscipline.ARTIFICIAL_INTELLIGENCE));
    }

    @Test
    void testAvgGradeInOneSemester() {
        student.getCreditBook().addGrade(3,
                AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(4,
                AcademicDiscipline.COMPUTATION_MODELS);
        student.getCreditBook().addGrade(5,
                AcademicDiscipline.PAK);
        assertEquals(student.getCreditBook().averageGrade(), 4.0);
    }

    @Test
    void testAvgGradeOnDifferentSemesters() {
        student.getCreditBook().addGrade(3,
                AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.finishSemester();
        student.getCreditBook().addGrade(4,
                AcademicDiscipline.COMPUTATION_MODELS);
        student.finishSemester();
        student.getCreditBook().addGrade(5,
                AcademicDiscipline.PAK);
        assertEquals(student.getCreditBook().averageGrade(), 4.0);
    }

    @Test
    void testIncreasedScholarshipInitialState() {
        assertFalse(student.getCreditBook().canObtainIncreasedScholarship());
    }

    @Test
    void testIncreasedScholarshipWithPerfectGrades() {
        student.getCreditBook().addGrade(5, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(5, AcademicDiscipline.COMPUTATION_MODELS);
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.PAK);
        student.getCreditBook().addGrade(5, AcademicDiscipline.OBJECT_ORIENTED_PROGRAMMING);
        student.finishSemester();
        assertTrue(student.getCreditBook().canObtainIncreasedScholarship());
    }

    @Test
    void testIncreasedScholarshipWithThreeInDifferentialDisciplines() {
        student.getCreditBook().addGrade(3, AcademicDiscipline.DECLARATIVE_PROGRAMMING);
        student.getCreditBook().addGrade(4, AcademicDiscipline.COMPUTATION_MODELS);
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.PAK);
        student.finishSemester();
        assertTrue(student.getCreditBook().canObtainIncreasedScholarship());
    }

    @Test
    void testIncreasedScholarshipWithSatisfactoryExamGrade() {
        student.getCreditBook().addGrade(3, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(4, AcademicDiscipline.COMPUTATION_MODELS);
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.PAK);
        assertFalse(student.getCreditBook().canObtainIncreasedScholarship());
    }

    @Test
    void testRedDiplomaWithAllPerfectGrades() {
        student.getCreditBook().addGrade(5, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(5, AcademicDiscipline.COMPUTATION_MODELS);
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.OBJECT_ORIENTED_PROGRAMMING);
        student.getCreditBook().addGrade(5, AcademicDiscipline.FINAL_THESIS);
        student.finishSemester();
        assertTrue(student.getCreditBook().canObtainRedDiploma());
    }

    @Test
    void testRedDiplomaWithSingleUnsatisfactoryGrade() {
        student.getCreditBook().addGrade(5, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(3, AcademicDiscipline.COMPUTATION_MODELS); // Unsatisfactory
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.OBJECT_ORIENTED_PROGRAMMING);
        student.getCreditBook().addGrade(5, AcademicDiscipline.FINAL_THESIS);
        student.finishSemester();
        assertFalse(student.getCreditBook().canObtainRedDiploma());
    }

    @Test
    void testRedDiplomaWithBadThesis() {
        student.getCreditBook().addGrade(5, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(5, AcademicDiscipline.COMPUTATION_MODELS);
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.OBJECT_ORIENTED_PROGRAMMING);
        student.getCreditBook().addGrade(4, AcademicDiscipline.FINAL_THESIS);
        student.finishSemester();
        assertFalse(student.getCreditBook().canObtainRedDiploma());
    }

    @Test
    void testRedDiplomaWithBorderlineAverageGrade() {
        student.getCreditBook().addGrade(5, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        student.getCreditBook().addGrade(4, AcademicDiscipline.COMPUTATION_MODELS);
        student.getCreditBook().addGrade(4, AcademicDiscipline.PAK);
        student.getCreditBook().addGrade(4, AcademicDiscipline.DECLARATIVE_PROGRAMMING);
        student.getCreditBook().addGrade(4, AcademicDiscipline.DIFFERENTIAL_EQUATIONS);
        student.getCreditBook().addGrade(4, AcademicDiscipline.IMPERATIVE_PROGRAMMING);
        student.getCreditBook().addGrade(4, AcademicDiscipline.OPERATING_SYSTEMS);
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.OBJECT_ORIENTED_PROGRAMMING);
        student.getCreditBook().addGrade(5, AcademicDiscipline.FINAL_THESIS);
        student.finishSemester();
        assertFalse(student.getCreditBook().canObtainRedDiploma());
    }
}
