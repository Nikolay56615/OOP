package ru.nsu.lebedev.pizzeria;

/**
 * RunnableBaker represents a baker running in its own thread.
 * <p>
 * The baker continuously attempts to accept new orders from the newOrders queue,
 * simulates cooking (using a delay), and then adds the cooked order to the warehouse.
 */
public class ThreadBaker implements TerminableEmployee {
    private final Baker bakerData;
    private final MyBlockingQueue<Order> newOrders;
    private Order currentOrder;
    private final MyBlockingQueue<Order> warehouse;
    private volatile boolean aboutToFinish = false;

    /**
     * Constructor.
     *
     * @param bakerData data about the baker
     * @param newOrders queue with new orders
     * @param warehouse warehouse queue for cooked orders
     */
    public ThreadBaker(Baker bakerData, MyBlockingQueue<Order> newOrders, MyBlockingQueue<Order> warehouse) {
        this.bakerData = bakerData;
        this.newOrders = newOrders;
        this.warehouse = warehouse;
    }

    @Override
    public void offerToFinishJob() {
        aboutToFinish = true;
    }

    /**
     * Attempts to retrieve an order from the newOrders queue.
     *
     * @return true if an order was successfully retrieved, false otherwise.
     */
    private boolean acceptOrder() {
        try {
            var optOrder = newOrders.poll();
            if (optOrder.isEmpty()) {
                return false;
            }
            currentOrder = optOrder.get();
        } catch (InterruptedException e) {
            System.out.println("Baker " + bakerData.name() + " interrupted while accepting order: " + e.getMessage());
            return false;
        }
        System.out.println("Baker " + bakerData.name() + " accepted order " + currentOrder.id());
        return true;
    }

    /**
     * Simulates cooking the current order.
     * On success, adds the cooked order to the warehouse; in case of error,
     * returns the order back to the newOrders queue.
     */
    private void cookOrder() {
        boolean error = false;
        try {
            Thread.sleep((long) bakerData.cookingTime() * Pizzeria.TIME_MS_QUANTUM);
        } catch (InterruptedException e) {
            System.out.println("Baker " + bakerData.name() +
                " interrupted while cooking order " + currentOrder.id() + ": " + e.getMessage());
            error = true;
        }
        if (!error) {
            System.out.println("Baker " + bakerData.name() + " cooked order " + currentOrder.id());
            try {
                warehouse.add(currentOrder);
                System.out.println("Baker " + bakerData.name() +
                    " placed order " + currentOrder.id() + " to warehouse");
            } catch (InterruptedException e) {
                System.out.println("Baker " + bakerData.name() +
                    " interrupted while adding order " + currentOrder.id() + " to warehouse: " + e.getMessage());
                error = true;
            } catch (IllegalStateException e) {
                System.out.println("Baker " + bakerData.name() +
                    " cannot place order " + currentOrder.id() + " to warehouse (queue closed): " + e.getMessage());
                error = true;
            }
        }
        if (error) {
            try {
                newOrders.add(currentOrder);
            } catch (InterruptedException e) {
                System.out.println("Baker " + bakerData.name() +
                    " interrupted while returning failed order " + currentOrder.id() + ": " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Baker " + bakerData.name() +
                    " cannot return failed order " + currentOrder.id() + " (queue closed): " + e.getMessage());
            }
        }
    }

    /**
     * Main loop for the baker thread.
     * Continuously accepts and cooks orders until signaled to finish.
     */
    @Override
    public void run() {
        while (!aboutToFinish) {
            if (acceptOrder()) {
                cookOrder();
            }
        }
        System.out.println("Baker " + bakerData.name() + " finished work.");
    }
}
