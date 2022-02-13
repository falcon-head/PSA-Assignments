/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.elementary.InsertionSort;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.Random;

import static edu.neu.coe.info6205.util.Utilities.formatWhole;

/**
 * This class implements a simple Benchmark utility for measuring the running time of algorithms.
 * It is part of the repository for the INFO6205 class, taught by Prof. Robin Hillyard
 * <p>
 * It requires Java 8 as it uses function types, in particular, UnaryOperator&lt;T&gt; (a function of T => T),
 * Consumer&lt;T&gt; (essentially a function of T => Void) and Supplier&lt;T&gt; (essentially a function of Void => T).
 * <p>
 * In general, the benchmark class handles three phases of a "run:"
 * <ol>
 *     <li>The pre-function which prepares the input to the study function (field fPre) (may be null);</li>
 *     <li>The study function itself (field fRun) -- assumed to be a mutating function since it does not return a result;</li>
 *     <li>The post-function which cleans up and/or checks the results of the study function (field fPost) (may be null).</li>
 * </ol>
 * <p>
 * Note that the clock does not run during invocations of the pre-function and the post-function (if any).
 *
 * @param <T> The generic type T is that of the input to the function f which you will pass in to the constructor.
 */
public class Benchmark_Timer<T> implements Benchmark<T> {

    /**
     * Calculate the appropriate number of warmup runs.
     *
     * @param m the number of runs.
     * @return at least 2 and at most m/10.
     */
    static int getWarmupRuns(int m) {
        return Integer.max(2, Integer.min(10, m / 10));
    }

    /**
     * Run function f m times and return the average time in milliseconds.
     *
     * @param supplier a Supplier of a T
     * @param m        the number of times the function f will be called.
     * @return the average number of milliseconds taken for each run of function f.
     */
    @Override
    public double runFromSupplier(Supplier<T> supplier, int m) {
        logger.info("Begin run: " + description + " with " + formatWhole(m) + " runs");
        // Warmup phase
        final Function<T, T> function = t -> {
            fRun.accept(t);
            return t;
        };
        new Timer().repeat(getWarmupRuns(m), supplier, function, fPre, null);

        // Timed phase
        return new Timer().repeat(m, supplier, function, fPre, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => .
     *                    Function fPre is ruTn before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun, Consumer<T> fPost) {
        this.description = description;
        this.fPre = fPre;
        this.fRun = fRun;
        this.fPost = fPost;
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun) {
        this(description, fPre, fRun, null);
    }

    /**
     * Constructor for a Benchmark_Timer with only fRun and fPost Consumer parameters.
     *
     * @param description the description of the benchmark.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, Consumer<T> fRun, Consumer<T> fPost) {
        this(description, null, fRun, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer where only the (timed) run function is specified.
     *
     * @param description the description of the benchmark.
     * @param f           a Consumer function (i.e. a function of T => Void).
     *                    Function f is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, Consumer<T> f) {
        this(description, null, f, null);
    }

    private final String description;
    private final UnaryOperator<T> fPre;
    private final Consumer<T> fRun;
    private final Consumer<T> fPost;

    final static LazyLogger logger = new LazyLogger(Benchmark_Timer.class);


    // Main function to run the benchmark
    public static void main(String[] args) {
        // Length of the array
        int n = 1000;
        //Total number of tests that are needed to be run
        int[] numberOfRuns = new int[]{2, 4, 6, 8, 10, 14};
        Benchmark_Timer<Boolean> benchTimer = new Benchmark_Timer<Boolean>("INSORT", new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {

            }
        });

        for(int i=0; i<numberOfRuns.length;i++)
        {
            System.out.println("Ordered " +benchTimer.ordered(numberOfRuns[i],n));
        }
        System.out.println("\n Next Program");

        //Reversed array
        for(int i=0; i<numberOfRuns.length;i++)
        {
            System.out.println("Reversed "+benchTimer.reversed(numberOfRuns[i],n));
        }
        System.out.println("\n Next program");

        //Partially ordered array
        for(int i=0; i<numberOfRuns.length;i++)
        {
            System.out.println("Partially Ordered "+benchTimer.partially(numberOfRuns[i],n));
        }
        System.out.println("\n Next Program");

        //Random ordered array
        for(int i=0; i<numberOfRuns.length;i++)
        {
            System.out.println("Random Ordered "+benchTimer.random(numberOfRuns[i],n));
        }
        System.out.println("\n Done");

    }

    //Created an ordered array
    private double ordered(int numberOfRuns, int n){
        Integer[] ordered = new Integer[n];
        for (int i = 0; i < n ; i++) {
            ordered[i] = i;
        }
        Benchmark<Boolean> benchTimer = new Benchmark_Timer<>(
                "INSORTTIMERTEST",
                null,
                b -> {
                    new InsertionSort<Integer>().sort(ordered,
                            0,ordered.length);
                },
                null
        );
        double x = benchTimer.run(true,numberOfRuns);
        return x;
    }



    //created an array with reversed order
    private double reversed(int numberOfRuns, int n){
        Integer[] reversed = new Integer[n];
        for (int i = 0; i < n ; i++) {
            reversed[i] = n-i-1;
        }
        Benchmark<Boolean> benchTimer = new Benchmark_Timer<>(
                "INSORTTIMERTEST",
                null,
                b -> {
                    new InsertionSort<Integer>().sort(reversed,
                            0,reversed.length);
                },
                null
        );
        double x = benchTimer.run(true,numberOfRuns);
        return x;
    }
    //created an array with partially order
    private double partially(int numberOfRuns, int n){
        Integer[] partially = new Integer[n];
        Random ran = new Random();
        for (int i = 0; i < n/2 ; i++) {
            partially[i] = ran.nextInt(n/2);
        }
        for (int i = n/2; i < n ; i++) {
            partially[i] = i;
        }

        Benchmark<Boolean> benchTimer = new Benchmark_Timer<>(
                "INSORTTIMERTEST",
                null,
                b -> {
                    new InsertionSort<Integer>().sort(partially,
                            0,partially.length);
                },
                null
        );
        double x = benchTimer.run(true,numberOfRuns);
        return x;
    }
    //created an array without an order
    private double random(int numberOfRuns, int n){
        Integer[] random = new Integer[n];
        Random ran = new Random();
        for (int i = 0; i < n ; i++) {
            random[i] = ran.nextInt(n);
        }

        Benchmark<Boolean> benchTimer = new Benchmark_Timer<>(
                "INSORTTIMERTEST",
                null,
                b -> {
                    new InsertionSort<Integer>().sort(random,
                            0,random.length);
                },
                null
        );
        double x = benchTimer.run(true,numberOfRuns);
        return x;
    }

}
