import React, { createContext, useContext, useState, useCallback, useEffect } from "react";
import { CartItem, Pizza } from "@/types";
import { toast } from "@/hooks/use-toast";
import { useAuth } from "./AuthContext";
import * as cartApi from "@/api/cart";

interface CartContextType {
  items: CartItem[];
  addToCart: (pizza: Pizza) => Promise<void>;
  removeFromCart: (pizzaId: string) => Promise<void>;
  updateQuantity: (pizzaId: string, quantity: number) => Promise<void>;
  clearCart: () => Promise<void>;
  totalItems: number;
  totalPrice: number;
  loading: boolean;
  refreshCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [items, setItems] = useState<CartItem[]>([]);
  const [loading, setLoading] = useState(false);
  const { user } = useAuth();

  // Fetch cart from backend when user logs in
  const refreshCart = useCallback(async () => {
    if (!user) {
      setItems([]);
      return;
    }

    try {
      setLoading(true);
      const cartData = await cartApi.getCart();

      // Convert backend response to CartItem format
      const cartItems: CartItem[] = cartData.items.map(item => ({
        pizza: {
          id: item.pizza.id,
          name: item.pizza.name,
          description: item.pizza.description,
          price: item.pizza.price,
          category: item.pizza.category,
          imageUrl: item.pizza.imageUrl,
          available: item.pizza.available,
        },
        quantity: item.quantity,
      }));

      setItems(cartItems);
    } catch (error: any) {
      console.error("Failed to fetch cart:", error);
      setItems([]);
    } finally {
      setLoading(false);
    }
  }, [user]);

  // Load cart when user changes
  useEffect(() => {
    refreshCart();
  }, [refreshCart]);

  const addToCart = useCallback(async (pizza: Pizza) => {
    if (!user) {
      toast({ title: "Please login", description: "You need to login to add items to cart.", variant: "destructive" });
      return;
    }

    try {
      await cartApi.addToCart(pizza.id);
      await refreshCart();
      toast({ title: "Added to cart", description: `${pizza.name} added to your cart.` });
    } catch (error: any) {
      toast({ title: "Error", description: error.message || "Failed to add to cart.", variant: "destructive" });
    }
  }, [user, refreshCart]);

  const removeFromCart = useCallback(async (pizzaId: string) => {
    try {
      await cartApi.removeFromCart(pizzaId);
      await refreshCart();
      toast({ title: "Removed", description: "Item removed from cart." });
    } catch (error: any) {
      toast({ title: "Error", description: error.message || "Failed to remove item.", variant: "destructive" });
    }
  }, [refreshCart]);

  const updateQuantity = useCallback(async (pizzaId: string, quantity: number) => {
    if (quantity < 1) return;

    try {
      await cartApi.updateCartItem(pizzaId, quantity);
      await refreshCart();
    } catch (error: any) {
      toast({ title: "Error", description: error.message || "Failed to update quantity.", variant: "destructive" });
    }
  }, [refreshCart]);

  const clearCart = useCallback(async () => {
    try {
      await cartApi.clearCart();
      setItems([]);
    } catch (error: any) {
      toast({ title: "Error", description: error.message || "Failed to clear cart.", variant: "destructive" });
    }
  }, []);

  const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
  const totalPrice = items.reduce((sum, item) => sum + item.pizza.price * item.quantity, 0);

  return (
    <CartContext.Provider value={{ items, addToCart, removeFromCart, updateQuantity, clearCart, totalItems, totalPrice, loading, refreshCart }}>
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) throw new Error("useCart must be used within CartProvider");
  return context;
};
