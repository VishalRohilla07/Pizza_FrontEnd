import api from './axios';

export interface PaymentIntentResponse {
    clientSecret: string;
    paymentIntentId: string;
    orderId: number;
}

/**
 * Create payment intent for order
 */
export const createPaymentIntent = async (orderId: string): Promise<PaymentIntentResponse> => {
    const response = await api.post(`/payment/create-intent?orderId=${orderId}`);
    return response.data;
};
