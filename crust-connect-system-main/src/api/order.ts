import api from './axios';
import { Order, OrderStatus } from '@/types';

export interface OrderItemResponse {
    id: number;
    pizza: {
        id: string;
        name: string;
        description: string;
        price: number;
        category: 'VEG' | 'NON_VEG';
        imageUrl: string;
        available: boolean;
    };
    quantity: number;
    price: number;
    subtotal: number;
}

export interface OrderResponse {
    id: string;
    userId: number;
    items: OrderItemResponse[];
    totalAmount: number;
    orderStatus: OrderStatus;
    paymentStatus: 'PENDING' | 'SUCCESS' | 'FAILED';
    paymentIntentId?: string;
    createdAt: string;
    updatedAt: string;
}

/**
 * Create order from cart
 */
export const createOrder = async (): Promise<OrderResponse> => {
    const response = await api.post('/orders');
    return response.data;
};

/**
 * Get user's orders
 */
export const getUserOrders = async (): Promise<OrderResponse[]> => {
    const response = await api.get('/orders');
    return response.data;
};

/**
 * Get order by ID
 */
export const getOrderById = async (id: string): Promise<OrderResponse> => {
    const response = await api.get(`/orders/${id}`);
    return response.data;
};

/**
 * Cancel order
 */
export const cancelOrder = async (id: string): Promise<OrderResponse> => {
    const response = await api.post(`/orders/${id}/cancel`);
    return response.data;
};

/**
 * Get all orders (Admin only)
 */
export const getAllOrders = async (page = 0, size = 20, status?: OrderStatus) => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
    });

    if (status) {
        params.append('status', status);
    }

    const response = await api.get(`/admin/orders?${params.toString()}`);
    return response.data;
};

/**
 * Update order status (Admin only)
 */
export const updateOrderStatus = async (id: string, status: OrderStatus): Promise<OrderResponse> => {
    const response = await api.put(`/admin/orders/${id}/status?status=${status}`);
    return response.data;
};
