import api from './axios';

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    type: string;
    id: number;
    name: string;
    email: string;
    role: 'CUSTOMER' | 'ADMIN';
}

/**
 * Register a new user
 */
export const register = async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/register', data);
    return response.data;
};

/**
 * Login user
 */
export const login = async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/login', data);
    return response.data;
};

/**
 * Store authentication data in localStorage
 */
export const storeAuth = (authData: AuthResponse) => {
    localStorage.setItem('token', authData.token);
    localStorage.setItem('user', JSON.stringify({
        id: authData.id,
        name: authData.name,
        email: authData.email,
        role: authData.role,
    }));
};

/**
 * Clear authentication data from localStorage
 */
export const clearAuth = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
};

/**
 * Get stored user data
 */
export const getStoredUser = () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
};

/**
 * Check if user is authenticated
 */
export const isAuthenticated = (): boolean => {
    return !!localStorage.getItem('token');
};
