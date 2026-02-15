import Navbar from "@/components/Navbar";
import { useCart } from "@/context/CartContext";
import { Button } from "@/components/ui/button";
import { Minus, Plus, Trash2, ShoppingBag } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";

const CartPage = () => {
  const { items, removeFromCart, updateQuantity, totalPrice, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleCheckout = () => {
    if (!user) {
      navigate("/login");
      return;
    }
    navigate("/checkout");
  };

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="container mx-auto flex flex-col items-center justify-center px-4 py-20 text-center">
          <ShoppingBag className="mb-4 h-16 w-16 text-muted-foreground/40" />
          <h2 className="mb-2 font-display text-2xl font-bold">Your cart is empty</h2>
          <p className="mb-6 text-muted-foreground">Add some delicious pizzas to get started!</p>
          <Link to="/menu">
            <Button>Browse Menu</Button>
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <div className="container mx-auto px-4 py-10">
        <h1 className="mb-8 font-display text-3xl font-bold">Your Cart</h1>

        <div className="grid gap-8 lg:grid-cols-3">
          <div className="space-y-4 lg:col-span-2">
            {items.map((item) => (
              <div key={item.pizza.id} className="flex items-center gap-4 rounded-xl border bg-card p-4">
                <img
                  src={item.pizza.imageUrl}
                  alt={item.pizza.name}
                  className="h-20 w-20 rounded-lg object-cover"
                />
                <div className="flex-1">
                  <h3 className="font-display font-semibold">{item.pizza.name}</h3>
                  <p className="text-sm text-muted-foreground">${item.pizza.price.toFixed(2)} each</p>
                </div>
                <div className="flex items-center gap-2">
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={() => updateQuantity(item.pizza.id, item.quantity - 1)}
                    disabled={item.quantity <= 1}
                  >
                    <Minus className="h-3 w-3" />
                  </Button>
                  <span className="w-8 text-center font-medium">{item.quantity}</span>
                  <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={() => updateQuantity(item.pizza.id, item.quantity + 1)}
                  >
                    <Plus className="h-3 w-3" />
                  </Button>
                </div>
                <span className="w-20 text-right font-display font-bold text-primary">
                  ${(item.pizza.price * item.quantity).toFixed(2)}
                </span>
                <Button variant="ghost" size="icon" onClick={() => removeFromCart(item.pizza.id)}>
                  <Trash2 className="h-4 w-4 text-destructive" />
                </Button>
              </div>
            ))}
          </div>

          <div className="rounded-xl border bg-card p-6">
            <h3 className="mb-4 font-display text-lg font-bold">Order Summary</h3>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Subtotal</span>
                <span>${totalPrice.toFixed(2)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-muted-foreground">Delivery</span>
                <span className="text-accent font-medium">Free</span>
              </div>
              <div className="border-t pt-2">
                <div className="flex justify-between text-lg font-bold">
                  <span>Total</span>
                  <span className="text-primary">${totalPrice.toFixed(2)}</span>
                </div>
              </div>
            </div>
            <Button onClick={handleCheckout} className="mt-6 w-full" size="lg">
              Proceed to Checkout
            </Button>
            <Button variant="ghost" onClick={clearCart} className="mt-2 w-full text-muted-foreground" size="sm">
              Clear Cart
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CartPage;
