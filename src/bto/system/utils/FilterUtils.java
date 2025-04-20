package bto.system.utils;

import bto.system.models.BTOProject;
import java.util.List;
import java.util.stream.Collectors;

public class FilterUtils {

    /**
     * Filters projects by neighborhood
     */
    public static List<BTOProject> filterByNeighborhood(List<BTOProject> projects, String neighborhood) {
        return projects.stream()
                .filter(p -> p.getNeighborhood().equalsIgnoreCase(neighborhood))
                .collect(Collectors.toList());
    }

    /**
     * Filters projects by flat type availability
     */
    public static List<BTOProject> filterByFlatType(List<BTOProject> projects, String flatType) {
        return projects.stream()
                .filter(p -> p.getFlatTypes().stream()
                        .anyMatch(ft -> ft.getType().equalsIgnoreCase(flatType)))
                .collect(Collectors.toList());
    }

    /**
     * Filters projects by application status (open/closed)
     */
    public static List<BTOProject> filterByApplicationStatus(List<BTOProject> projects, boolean acceptingApplications) {
        return projects.stream()
                .filter(p -> p.isAcceptingApplications() == acceptingApplications)
                .collect(Collectors.toList());
    }

    /**
     * Combined filter with multiple criteria
     */
    public static List<BTOProject> advancedFilter(
            List<BTOProject> projects,
            String neighborhood,
            String flatType,
            boolean onlyVisible
    ) {
        return projects.stream()
                .filter(p -> !onlyVisible || p.isVisible())
                .filter(p -> neighborhood == null || p.getNeighborhood().equalsIgnoreCase(neighborhood))
                .filter(p -> flatType == null || p.getFlatTypes().stream()
                        .anyMatch(ft -> ft.getType().equalsIgnoreCase(flatType)))
                .collect(Collectors.toList());
    }
}
