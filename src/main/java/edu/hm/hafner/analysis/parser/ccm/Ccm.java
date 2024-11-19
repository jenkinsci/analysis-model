package edu.hm.hafner.analysis.parser.ccm;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Entity used by {@link CcmParser} to represent the root node of CCM results file.
 *
 * @author Bruno P. Kinoshita - http://www.kinoshita.eti.br
 * @since 1.0
 */
@SuppressWarnings("all")
@SuppressFBWarnings("EI")
public class Ccm {
    /**
     * List of metrics present in the XML file.
     */
    private List<Metric> metrics = new ArrayList<>();

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public void addMetric(Metric metric) {
        this.metrics.add(metric);
    }
}
