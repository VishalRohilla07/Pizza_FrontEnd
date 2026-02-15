import api from './axios';

export interface CartItemResponse {
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
    subtotal: number;
}

export interface CartResponse {
    id: number;
    items: CartItemResponse[];
    totalItems: number;
    totalPrice: number;
}

/**
 * Get user's cart
 */
export const getCart = async (): Promise<CartResponse> => {
    const response = await api.get('/cart');
    return response.data;
};

/**
 * Add item to cart
 */
export const addToCart = async (pizzaId: string): Promise<CartResponse> => {
    const response = await api.post('/cart/items', { pizzaId: Number(pizzaId) });
    return response.data;
};

/**
 * Update cart item quantity
 */
export const updateCartItem = async (pizzaId: string, quantity: number): Promise<CartResponse> => {
    const response = await api.put(`/cart/items/${pizzaId}?quantity=${quantity}`);
    return response.data;
};

/**
 * Remove item from cart
 */
export const removeFromCart = async (pizzaId: string): Promise<CartResponse> => {
    const response = await api.delete(`/cart/items/${pizzaId}`);
    return response.data;
};

/**
 * Clear cart
 */
export const clearCart = async (): Promise<void> => {
    await api.delete('/cart');
};
