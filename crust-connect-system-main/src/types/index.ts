export type PizzaCategory = "VEG" | "NON_VEG";

export interface Pizza {
  id: string;
  name: string;
  description: string;
  price: number;
  category: PizzaCategory;
  imageUrl: string;
  available: boolean;
}

export interface CartItem {
  pizza: Pizza;
  quantity: number;
}

export type OrderStatus = "PLACED" | "CONFIRMED" | "PREPARING" | "OUT_FOR_DELIVERY" | "DELIVERED" | "CANCELLED";
export type PaymentStatus = "PENDING" | "SUCCESS" | "FAILED";

export interface OrderItem {
  pizza: Pizza;
  quantity: number;
  price: number;
}

export interface Order {
  id: string;
  items: OrderItem[];
  totalAmount: number;
  orderStatus: OrderStatus;
  paymentStatus: PaymentStatus;
  createdAt: string;
  updatedAt: string;
}

export type UserRole = "CUSTOMER" | "ADMIN";

export interface User {
  id: string;
  name: string;
  email: string;
  role: UserRole;
}
