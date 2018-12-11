package by.bsuir.course.entities;

import java.io.Serializable;
import java.util.*;

public class Sportsman extends Human implements Serializable {
    private SoloSport performance;

    public SoloSport getPerformance() {
        return performance;
    }

    public void setPerformance(SoloSport performance) {
        this.performance = performance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Sportsman sportsman = (Sportsman) o;
        return Objects.equals(performance, sportsman.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), performance);
    }
}
