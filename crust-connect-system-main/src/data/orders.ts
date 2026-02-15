import { Order } from "@/types";
import { pizzas } from "./pizzas";

export const sampleOrders: Order[] = [
  {
    id: "ORD-001",
    items: [
      { pizza: pizzas[0], quantity: 2, price: 12.99 },
      { pizza: pizzas[1], quantity: 1, price: 15.99 },
    ],
    totalAmount: 41.97,
    orderStatus: "DELIVERED",
    paymentStatus: "SUCCESS",
    createdAt: "2026-02-12T18:30:00Z",
    updatedAt: "2026-02-12T19:15:00Z",
  },
  {
    id: "ORD-002",
    items: [
      { pizza: pizzas[4], quantity: 1, price: 14.99 },
    ],
    totalAmount: 14.99,
    orderStatus: "PREPARING",
    paymentStatus: "SUCCESS",
    createdAt: "2026-02-14T12:00:00Z",
    updatedAt: "2026-02-14T12:10:00Z",
  },
];
