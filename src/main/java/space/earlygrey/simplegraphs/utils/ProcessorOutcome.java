package space.earlygrey.simplegraphs.utils;


/**
 * Represents the outcome of a preprocessor process call. See {@link SearchPreprocessor} or {@link ShortestPathPreProcessor}.
 */
public enum ProcessorOutcome {

    /**
     * Continue processing this vertex as normal.
     */
    CONTINUE,

    /**
     * Do not process this vertex and continue running the algorithm.
     */
    IGNORE,

    /**
     * Immediately terminate the algorithm.
     */
    TERMINATE
}
