package bg.sofia.uni.fmi.mjt.carwash.plan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WashPlanTest {

    private List<WashStep> steps;

    @BeforeEach
    void setUp() {
        steps = new ArrayList<>();
        steps.add(WashStep.RINSE);
    }

    @Test
    void testWashPlanWithNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan(null, 600, steps),
                "Null name should be rejected");
    }

    @Test
    void testWashPlanWithEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("", 600, steps),
                "Empty name should be rejected");
    }

    @Test
    void testWashPlanWithBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("   ", 600, steps),
                "Blank name should be rejected");
    }

    @Test
    void testWashPlanWithZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("Basic", 0, steps),
                "Zero price should be rejected");
    }

    @Test
    void testWashPlanWithNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("Basic", -100, steps),
                "Negative price should be rejected");
    }

    @Test
    void testWashPlanWithNullSteps() {
        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("Basic", 600, null),
                "Null steps should be rejected");
    }

    @Test
    void testWashPlanWithEmptySteps() {
        steps.clear();

        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("Basic", 600, steps),
                "Empty steps should be rejected");
    }

    @Test
    void testWashPlanWithNullStep() {
        steps.add(null);

        assertThrows(IllegalArgumentException.class,
                () -> new WashPlan("Basic", 600, steps),
                "Null step should be rejected");
    }

    @Test
    void testWashPlanWithValidArguments() {
        assertDoesNotThrow(
                () -> new WashPlan("Basic", 600, steps),
                "Valid arguments should be accepted");
    }

    @Test
    void testWashPlanStepsAreUnmodifiable() {
        WashPlan plan = new WashPlan("Basic", 600, steps);

        assertThrows(UnsupportedOperationException.class,
                () -> plan.steps().add(WashStep.DRY),
                "Steps should be unmodifiable");
    }

    @Test
    void testWashPlanDefensiveCopy() {
        WashPlan plan = new WashPlan("Basic", 600, steps);

        steps.add(WashStep.DRY);

        assertEquals(1, plan.steps().size(),
                "Original list changes should not affect the plan");
    }

    @Test
    void testWashPlanPreservesRepeatedStepsAndOrder() {
        steps.add(WashStep.SHAMPOO);
        steps.add(WashStep.RINSE);

        List<WashStep> expected = new ArrayList<>(steps);
        WashPlan plan = new WashPlan("Basic", 600, steps);

        assertEquals(expected, plan.steps(),
                "Step order and duplicates should be preserved");
    }
}
