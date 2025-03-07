package ru.nsu.lebedev.pizzeria;

import java.util.LinkedList;
import java.util.Optional;

/**
 * RunnableCourier represents a courier running in its own thread.
 * <p>
 * The courier repeatedly attempts to fill its trunk by polling orders from the warehouse queue.
 * Then, it delivers orders (simulated with a delay based on order deliveryTime).
 * In case of delivery errors, undelivered orders are returned to the newOrders queue.
 */
public class ThreadCourier implements TerminableEmployee {
    private final Courier courierData;
    private final MyBlockingQueue<Order> newOrders;
    private final MyBlockingQueue<Order> warehouse;
    private final LinkedList<Order> trunk;
    private volatile boolean aboutToFinish = false;

    /**
     * Constructor.
     *
     * @param courierData data about the courier
     * @param newOrders queue with new orders (for returning undelivered orders)
     * @param warehouse warehouse queue containing orders ready for delivery
     */
    public ThreadCourier(Courier courierData, MyBlockingQueue<Order> newOrders, MyBlockingQueue<Order> warehouse) {
        this.courierData = courierData;
        this.newOrders = newOrders;
        this.warehouse = warehouse;
        this.trunk = new LinkedList<>();
    }

    @Override
    public void offerToFinishJob() {
        aboutToFinish = true;
    }

    /**
     * Checks if the courier's trunk is full based on its capacity.
     *
     * @return true if trunk is full, false otherwise.
     */
    private boolean isTrunkFull() {
        return trunk.size() >= courierData.capacity();
    }

    /**
     * Fills the trunk by polling orders from the warehouse.
     * Uses a timeout to prevent indefinite waiting.
     */
    private void fillTrunk() {
        while (!isTrunkFull()) {
            try {
                Optional<Order> optOrder = warehouse.poll(Pizzeria.ORDER_WAITING_MS);
                if (optOrder.isEmpty()) {
                    break;
                }
                Order order = optOrder.get();
                trunk.add(order);
                System.out.println("Courier " + courierData.name() +
                    " got order " + order.id() + " from warehouse");
            } catch (InterruptedException e) {
                System.out.println("Courier " + courierData.name() +
                    " interrupted while polling warehouse: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Attempts to deliver all orders in the trunk.
     *
     * @return true if delivery succeeded for all orders, false if interrupted.
     */
    private boolean deliverOrders() {
        while (!trunk.isEmpty()) {
            Order order = trunk.poll();
            try {
                Thread.sleep((long) order.deliveryTime() * Pizzeria.TIME_MS_QUANTUM);
                System.out.println("Courier " + courierData.name() + " delivered order " + order.id());
            } catch (InterruptedException e) {
                System.out.println("Courier " + courierData.name() +
                    " interrupted while delivering order " + order.id() + ": " + e.getMessage());
                trunk.add(order);
                return false;
            }
        }
        return true;
    }

    /**
     * Returns undelivered orders from the trunk back to the newOrders queue.
     */
    private void returnOrders() {
        for (Order order : trunk) {
            try {
                newOrders.add(order);
            } catch (InterruptedException e) {
                System.out.println("Courier " + courierData.name() +
                    " interrupted while returning order " + order.id() + ": " + e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println("Courier " + courierData.name() +
                    " cannot return order " + order.id() + " (queue closed): " + e.getMessage());
            }
        }
        trunk.clear();
    }

    /**
     * Main loop for the courier thread.
     * Continues filling trunk and delivering orders until signaled to finish
     * and the warehouse becomes empty.
     */
    @Override
    public void run() {
        while (!aboutToFinish || !warehouse.isEmpty()) {
            fillTrunk();
            if (!deliverOrders()) {
                returnOrders();
            }
        }
        System.out.println("Courier " + courierData.name() + " finished work.");
    }
}
