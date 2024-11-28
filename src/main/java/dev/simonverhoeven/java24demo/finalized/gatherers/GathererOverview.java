package dev.simonverhoeven.java24demo.finalized.gatherers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

// JEP 489 Vector API - Ninth incubation


public class GathererOverview {
    public static <T> Gatherer.Integrator<Void, T, T> passthroughIntegrator() {
        return (_, element, downstream) -> {
            downstream.push(element);
            return true;
        };
    }

    public static <T> Gatherer.Integrator<Void, T, T> filter(Predicate<? super  T> filter) {
        return (_, element, downstream) -> {
            if (filter.test(element))
                downstream.push(element);
            return true;
        };
    }

    public static <T> Gatherer<T, ?, T> limit(int numberOfElements) {
        Supplier<AtomicInteger> initializer = AtomicInteger::new;
        Gatherer.Integrator<AtomicInteger, T, T> integrator =
                (state, element, downstream) -> {
                    var currentIndex = state.getAndIncrement();
                    if (currentIndex < numberOfElements) {
                        downstream.push(element);
                    }
                    return currentIndex + 1 < numberOfElements;
                };
        return Gatherer.ofSequential(initializer, integrator);
    }

    // finisher
    // receives states after stream element processing & downstream, potentially emits further elements downstream dependent on the status
    public static <T> BiConsumer<List<T>, Gatherer.Downstream<List<T>>> finisher() {
        return (state, downstream) -> {
            if (!state.isEmpty()) {
                downstream.push(List.copyOf(state));
            }
        };
    }

    // combiner
    // Needed when using a stateful gatherer in parallel. It combines 2 statuses during the join phase of parallel stream processing.
    public static BinaryOperator<AtomicReference<Integer>> lowestNumberCombiner() {
        return (firstState, secondState) -> {
            final var firstElement = firstState.get();
            final var secondElement = secondState.get();

            if (secondElement == null) {
                return firstState;
            } else if (firstElement == null) {
                return secondState;
            } else {
                return firstElement > secondElement ? secondState : firstState;
            }
        };
    }

    public static Gatherer<Integer, AtomicReference<Integer>, Integer> lowestGatherer() {
        return Gatherer.of(
                // Initializer
                AtomicReference::new,
                // Gatherer
                (state, element, _) -> {
                    Integer lowest = state.get();
                    if (lowest == null || element <= lowest) {
                        state.set(element);
                    }
                    return true;
                },
                // Combiner
                lowestNumberCombiner(),
                // Finisher
                (state, downstream) -> {
                    Integer lowest = state.get();
                    if (lowest != null) {
                        downstream.push(lowest);
                    }
                });
    }

    public static void main() {
        final var foldResult = IntStream.range(1, 10).boxed()
                .gather(
                        Gatherers.fold(() -> "0", (string, number) -> string + "-" +  number)
                )
                .findFirst();

        System.out.println("Folded: " + foldResult.orElse("empty"));

        final var squareResult = IntStream.range(1, 10).boxed()
                .gather(
                        Gatherers.mapConcurrent(5, number -> number*number)
                )
                .toList();

        System.out.println("Map concurrent: " + squareResult);

        final var scanResult = IntStream.range(1, 10).boxed()
                .gather(
                        Gatherers.scan(() -> 0, Integer::sum)
                )
                .toList();

        System.out.println("Prefix scan - incremental reduction: " + scanResult);

        final var windowFixedResult = IntStream.range(1, 10).boxed()
                .gather(Gatherers.windowFixed(2))
                .toList();

        System.out.println("Window fixed: " + windowFixedResult);

        final var windowSlidingResult = IntStream.range(1, 10).boxed()
                .gather(Gatherers.windowSliding(3))
                .toList();

        System.out.println("Window sliding: " + windowSlidingResult);

        final var customLimit = IntStream.range(1, 10).boxed()
                .gather(
                        limit(3)
                ).toList();

        System.out.println("Custom limit: " + customLimit);

        final var passthrough = IntStream.range(1, 10).boxed()
                .gather(
                        Gatherer.of(passthroughIntegrator())
                ).toList();

        System.out.println("Passthrough: " + passthrough);

        final var evenFilter = IntStream.range(1, 10).boxed()
                .gather(
                        Gatherer.of(filter(i -> i%2 == 0))
                ).toList();

        System.out.println("Even filter: " + evenFilter);

        final var lowestResult = List.of(1,2,3,4,5,6,7,8,9,10).parallelStream().gather(lowestGatherer()).findFirst();
        System.out.println("Using finisher and combiner result: " + lowestResult.orElseThrow(IllegalStateException::new));
    }
}
