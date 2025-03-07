package ru.nsu.lebedev.pizzeria;

/**
 * RunnableBaker represents a baker running in its own thread.
 * The baker continuously attempts to accept new orders from the pendingOrders queue,
 * simulates cooking (using a delay), and then adds the cooked order to the storageQueue.
 */
public class ThreadBaker implements TerminableEmployee {
    private final Baker bakerInfo;
    private final MyBlockingQueue<Order> pendingOrders;
    private Order activeOrder;
    private final MyBlockingQueue<Order> storageQueue;
    private volatile boolean shouldTerminate = false;

    /**
     * Constructor.
     *
     * @param bakerInfo   data about the baker
     * @param pendingOrders queue with new orders
     * @param storageQueue  storage queue for cooked orders
     */
    public ThreadBaker(Baker bakerInfo, MyBlockingQueue<Order> pendingOrders,
                       MyBlockingQueue<Order> storageQueue) {
        this.bakerInfo = bakerInfo;
        this.pendingOrders = pendingOrders;
        this.storageQueue = storageQueue;
    }

    @Override
    public void offerToFinishJob() {
        shouldTerminate = true;
    }

    /**
     * Attempts to retrieve an order from the pendingOrders queue.
     *
     * @return true if an order was successfully retrieved, false otherwise.
     */
    private boolean fetchOrder() {
        try {
            var optionalOrder = pendingOrders.poll();
            if (optionalOrder.isEmpty()) {
                return false;
            }
            activeOrder = optionalOrder.get();
        } catch (InterruptedException e) {
            System.out.println("Baker " + bakerInfo.name()
                + " interrupted while fetching order: " + e.getMessage());
            return false;
        }
        System.out.println("Baker " + bakerInfo.name() + " fetched order " + activeOrder.id());
        return true;
    }

    /**
     * Simulates cooking the active order.
     * On success, adds the cooked order to the storageQueue; in case of error,
     * returns the order back to the pendingOrders queue.
     */
    private void processOrder() {
        boolean hasError = false;
        try {
            Thread.sleep((long) bakerInfo.cookingTime() * Pizzeria.TIME_STEP_MS);
        } catch (InterruptedException e) {
            System.out.println("Baker " + bakerInfo.name()
                + " interrupted while processing order "
                + activeOrder.id() + ": " + e.getMessage());
            hasError = true;
        }
        if (!hasError) {
            System.out.println("Baker " + bakerInfo.name()
                + " processed order " + activeOrder.id());
            try {
                storageQueue.add(activeOrder);
                System.out.println("Baker " + bakerInfo.name()
                    + " placed order " + activeOrder.id() + " in storage queue");
            } catch (InterruptedException e) {
                System.out.println("Baker " + bakerInfo.name()
                    + " interrupted while adding order "
                    + activeOrder.id() + " to storage queue: " + e.getMessage());
                hasError = true;
            } catch (IllegalStateException e) {
                System.out.println("Baker " + bakerInfo.name()
                    + " cannot place order "
                    + activeOrder.id() + " in storage queue (queue closed): "
                    + e.getMessage());
                hasError = true;
            }
        }
        if (hasError) {
            try {
                pendingOrders.add(activeOrder);
            } catch (InterruptedException e) {
                System.out.println("Baker " + bakerInfo.name()
                    + " interrupted while returning failed order "
                    + activeOrder.id() + ": " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Baker " + bakerInfo.name()
                    + " cannot return failed order " + activeOrder.id()
                    + " (queue closed): " + e.getMessage());
            }
        }
    }

    /**
     * Main loop for the baker thread.
     * Continuously fetches and processes orders until signaled to terminate.
     */
    @Override
    public void run() {
        while (!shouldTerminate) {
            if (fetchOrder()) {
                processOrder();
            }
        }
        System.out.println("Baker " + bakerInfo.name() + " finished work.");
    }
}
