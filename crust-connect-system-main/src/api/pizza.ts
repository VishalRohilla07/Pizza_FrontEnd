import api from './axios';
import { Pizza } from '@/types';

export interface PizzaRequest {
    name: string;
    description: string;
    price: number;
    category: 'VEG' | 'NON_VEG';
    imageUrl: string;
    available: boolean;
}

/**
 * Get all available pizzas
 */
export const getAllPizzas = async (): Promise<Pizza[]> => {
    const response = await api.get('/pizzas');
    return response.data;
};

/**
 * Get pizza by ID
 */
export const getPizzaById = async (id: string): Promise<Pizza> => {
    const response = await api.get(`/pizzas/${id}`);
    return response.data;
};

/**
 * Create new pizza (Admin only)
 */
export const createPizza = async (data: PizzaRequest): Promise<Pizza> => {
    const response = await api.post('/pizzas', data);
    return response.data;
};

/**
 * Update pizza (Admin only)
 */
export const updatePizza = async (id: string, data: PizzaRequest): Promise<Pizza> => {
    const response = await api.put(`/pizzas/${id}`, data);
    return response.data;
};

/**
 * Delete pizza (Admin only)
 */
export const deletePizza = async (id: string): Promise<void> => {
    await api.delete(`/pizzas/${id}`);
};
