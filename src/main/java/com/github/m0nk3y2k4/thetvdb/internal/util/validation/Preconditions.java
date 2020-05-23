package com.github.m0nk3y2k4.thetvdb.internal.util.validation;

import com.github.m0nk3y2k4.thetvdb.internal.exception.APIPreconditionException;
import com.github.m0nk3y2k4.thetvdb.internal.util.APIUtil;

import java.util.function.Predicate;

/**
 * Collection of simple checks to be used to check for method invocation preconditions
 * <p/>
 * The default behavior for precondition checks provided by this class is to throw an {@link APIPreconditionException} in case the given
 * arguments do not match the requirements. Some of the methods may support extended control over the actual type of exception that
 * will be thrown by allowing the calling instance to privide it's onw runtime exception instance.
 * <p/>
 * Please note that this class should primarily be used for non-method parameter related precondition/object state checks as the default
 * <code>APIPreconditionException</code> indicates that resolving the failed validation isn't necessarily within the responsibility of the
 * calling instance (although it can be). A common precondition check would be for example to check whether the invoked object is in a proper
 * state in order to process the invocation. If not, this may possibly be resolved by the calling instance by e.g. some additional object
 * initialization. However, it may also prove to be unresolvable for example when the invoked method is only supported for certain object
 * states. To verify the values of given method parameters use {@link Parameters}.
 */
public final class Preconditions {

    private Preconditions() {}     // Hidden constructor. Only static methods

    /**
     * Checks the condition and throws the given runtime exception in case the condition is not met.
     *
     * @param condition The condition to check for
     * @param value The value to check against the codition
     * @param exception The exception to be thrown in case the condition is not met
     *
     * @param <T> the type of the value to check
     * @param <X> type of the runtime exception to be thrown
     */
    public static <T, X extends RuntimeException> void requires(Predicate<T> condition, T value, X exception) {
        if (!condition.test(value)) {
            throw exception;
        }
    }

    /**
     * Checks that the given <code>obj</code> is not <i>null</i>. Otherwise an exception with the given error message
     * will be thrown.
     *
     * @param obj The object to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws APIPreconditionException If the given <code>obj</code> is <i>null</i>
     */
    public static void requireNonNull(Object obj, String message) {
        if (obj == null) {
            throw new APIPreconditionException(message);
        }
    }

    /**
     * Checks that the given String is neither <i>null</i> nor empty. Otherwise an exception with the given error message
     * will be thrown.
     *
     * @param obj The String to check
     * @param message Error message to be propagated to the exception in case of a failed validation
     *
     * @throws APIPreconditionException If the given String is either <i>null</i> or empty
     */
    public static void requireNonEmpty(String obj, String message) {
        if (APIUtil.hasNoValue(obj)) {
            throw new APIPreconditionException(message);
        }
    }
}
