package ru.nsu.lebedev.pizzeria;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Pizzeria class for managing the pizzeria's operations and thread launching.
 */
public class Pizzeria implements Runnable {
    public static final long TIME_MS_QUANTUM = 100; // One time step in pizzeria's world
    public static final long ORDER_WAITING_MS = 1000; // Waiting time for orders in milliseconds

    private final long timeForClosing;
    private volatile boolean finished = false;

    private ArrayList<Baker> bakers;
    private EmployeesManager<ThreadBaker> bakersManager;
    private ArrayList<Courier> couriers;
    private EmployeesManager<ThreadCourier> couriersManager;
    private MyBlockingQueue<Order> warehouse;
    private MyBlockingQueue<Order> newOrders;

    private final String setupSavePath;

    /**
     * Constructor.
     * <br>
     * Loads data from the specified load path and initializes internal data.
     *
     * @param timeForClosing time in conventional units
     * @param setupLoadPath  path to initialize json setup
     * @param setupSavePath  path for final json setup
     * @throws IOException                when any error occurs while parsing input json setup
     * @throws IllegalArgumentException when input json setup has some illegal data
     */
    public Pizzeria(long timeForClosing, String setupLoadPath, String setupSavePath)
            throws IOException, IllegalArgumentException {
        System.out.println("Initializing Pizzeria");
        this.timeForClosing = timeForClosing;
        this.setupSavePath = setupSavePath;
        Setup setup = getSetup(setupLoadPath);
        checkSetupValidness(setup);
        bakers = setup.bakers();
        couriers = setup.couriers();
        warehouse = new MyBlockingQueue<>(setup.warehouseCapacity());
        newOrders = new MyBlockingQueue<>(setup.orders());
        System.out.println("Pizzeria initialized");
    }

    /**
     * Checks if the pizzeria has not finished its work day.
     *
     * @return true if the pizzeria is still running, false otherwise
     */
    public boolean hasNotFinished() {
        return !finished;
    }

    /**
     * Loads the setup configuration from the specified path.
     *
     * @param setupPath path to the JSON setup file
     * @return the parsed Setup object
     * @throws IOException if an error occurs while loading or parsing the setup
     */
    private Setup getSetup(String setupPath) throws IOException {
        System.out.println("Loading setup from " + setupPath);
        InputStream inputStream;
        Setup loadedSetup = null;
        try {
            inputStream = new FileInputStream(setupPath);
        } catch (IOException notFoundException) {
            inputStream = getClass().getClassLoader().getResourceAsStream(setupPath);
        }
        if (inputStream == null) {
            throw new FileNotFoundException(
                    "Couldn't find setup JSON file either in specified path "
                            + "or in this path in resources using path " + setupPath);
        }
        try {
            loadedSetup = Json.parse(inputStream, Setup.class);
        } catch (ParsingException e) {
            System.out.println("Failed to parse setup: " + e.getMessage());
            throw e;
        }
        inputStream.close();
        System.out.println("Loaded setup from " + setupPath);
        return loadedSetup;
    }

    /**
     * Saves the current setup to the specified path.
     */
    private void saveSetup() {
        System.out.println("Saving setup in " + setupSavePath);
        Setup setup = new Setup(bakers, couriers, warehouse.maxSize(), newOrders.getListCopy());
        try (OutputStream outputStream = new FileOutputStream(setupSavePath)) {
            Json.serialize(setup, outputStream);
            System.out.println("Saved setup in " + setupSavePath);
        } catch (IOException streamSerializeException) {
            System.out.println("Failed to save setup: " + streamSerializeException.getMessage());
            try {
                System.out.println(Json.serialize(setup));
            } catch (IOException stringSerializeException) {
                System.out.println("Failed to serialize setup: " + stringSerializeException.getMessage());
            }
        }
    }

    /**
     * Validates the setup data.
     *
     * @param setup the setup object to validate
     * @throws IllegalArgumentException if the setup data is invalid
     */
    private void checkSetupValidness(Setup setup) throws IllegalArgumentException {
        if (setup.bakers().isEmpty()) {
            System.out.println("Invalid setup: no bakers found");
            throw new IllegalArgumentException("There are no bakers");
        }
        if (setup.couriers().isEmpty()) {
            System.out.println("Invalid setup: no couriers found");
            throw new IllegalArgumentException("There are no couriers");
        }
        if (setup.warehouseCapacity() <= 0) {
            System.out.println("Invalid setup: non-positive warehouse capacity");
            throw new IllegalArgumentException("There is invalid warehouse capacity");
        }
    }

    /**
     * Runs the pizzeria operations.
     */
    @Override
    public void run() {
        System.out.println("Starting Pizzeria");
        runEmployees();
        System.out.println("All employees have started");
        try {
            Thread.sleep(timeForClosing * TIME_MS_QUANTUM);
        } catch (InterruptedException e) {
            System.out.println("Pizzeria was interrupted while waiting for work day to finish");
        }
        System.out.println("Finishing work day");
        finishWorkDay();
        System.out.println("Work day finished");
    }

    /**
     * Starts the employees (bakers and couriers) using EmployeesManager.
     */
    private void runEmployees() {
        ArrayList<ThreadBaker> runnableBakers = new ArrayList<>();
        for (var baker : bakers) {
            runnableBakers.add(new ThreadBaker(baker, newOrders, warehouse));
        }
        bakersManager = new EmployeesManager<>(runnableBakers);
        bakersManager.startEmployees();

        ArrayList<ThreadCourier> runnableCouriers = new ArrayList<>();
        for (var courier : couriers) {
            runnableCouriers.add(new ThreadCourier(courier, newOrders, warehouse));
        }
        couriersManager = new EmployeesManager<>(runnableCouriers);
        couriersManager.startEmployees();
    }

    /**
     * Finishes the work day by shutting down employees and saving the setup.
     */
    private void finishWorkDay() {
        bakersManager.offerEmployeesFinishJob();
        newOrders.close(); // Close the newOrders queue to prevent further additions
        try {
            bakersManager.shutdownAndAwaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException interruptedException) {
            System.out.println("Bakers waiting interrupted");
        }
        warehouse.close(); // Close the warehouse queue to prevent further additions
        System.out.println("All bakers have finished their job");

        couriersManager.offerEmployeesFinishJob();
        try {
            couriersManager.shutdownAndAwaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException interruptedException) {
            System.out.println("Couriers waiting interrupted");
        }
        System.out.println("All couriers finished their job");

        saveSetup();
        finished = true;
    }
}