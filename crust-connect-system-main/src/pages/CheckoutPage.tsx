import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useCart } from "@/context/CartContext";
import { useAuth } from "@/context/AuthContext";
import Navbar from "@/components/Navbar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { ShoppingBag, CreditCard, Loader2 } from "lucide-react";
import * as orderApi from "@/api/order";
import * as paymentApi from "@/api/payment";
import { loadStripe } from "@stripe/stripe-js";
import { Elements, PaymentElement, useStripe, useElements } from "@stripe/react-stripe-js";
import { toast } from "@/hooks/use-toast";

// Initialize Stripe (replace with your publishable key)
const stripePromise = loadStripe("pk_test_YOUR_STRIPE_PUBLISHABLE_KEY");

const CheckoutForm = ({ orderId, onSuccess }: { orderId: string; onSuccess: () => void }) => {
  const stripe = useStripe();
  const elements = useElements();
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!stripe || !elements) {
      return;
    }

    setLoading(true);

    const { error } = await stripe.confirmPayment({
      elements,
      confirmParams: {
        return_url: `${window.location.origin}/orders`,
      },
      redirect: "if_required",
    });

    if (error) {
      toast({
        title: "Payment failed",
        description: error.message,
        variant: "destructive",
      });
    } else {
      toast({
        title: "Payment successful!",
        description: "Your order has been placed.",
      });
      onSuccess();
    }

    setLoading(false);
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <PaymentElement />
      <Button type="submit" className="w-full" size="lg" disabled={!stripe || loading}>
        {loading ? (
          <>
            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            Processing...
          </>
        ) : (
          <>
            <CreditCard className="mr-2 h-4 w-4" />
            Pay Now
          </>
        )}
      </Button>
    </form>
  );
};

const CheckoutPage = () => {
  const { items, totalPrice, clearCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [orderId, setOrderId] = useState<string | null>(null);
  const [clientSecret, setClientSecret] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  if (!user) {
    navigate("/login");
    return null;
  }

  if (items.length === 0) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="container mx-auto px-4 py-16">
          <Card>
            <CardContent className="flex flex-col items-center justify-center py-16">
              <ShoppingBag className="h-16 w-16 text-muted-foreground mb-4" />
              <h2 className="text-xl font-semibold mb-2">Your cart is empty</h2>
              <p className="text-muted-foreground mb-4">Add some pizzas to get started!</p>
              <Button onClick={() => navigate("/menu")}>Browse Menu</Button>
            </CardContent>
          </Card>
        </div>
      </div>
    );
  }

  const handlePlaceOrder = async () => {
    try {
      setLoading(true);

      // Create order
      const order = await orderApi.createOrder();
      setOrderId(order.id);

      // Create payment intent
      const paymentIntent = await paymentApi.createPaymentIntent(order.id);
      setClientSecret(paymentIntent.clientSecret);

      toast({
        title: "Order created",
        description: "Please complete payment to confirm your order.",
      });
    } catch (err: any) {
      toast({
        title: "Error",
        description: err.message || "Failed to create order",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const handlePaymentSuccess = () => {
    clearCart();
    navigate("/orders");
  };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <h1 className="font-display text-3xl font-bold mb-8">Checkout</h1>

        <div className="grid gap-8 lg:grid-cols-3">
          {/* Order Summary */}
          <div className="lg:col-span-2">
            <Card>
              <CardHeader>
                <CardTitle>Order Summary</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {items.map((item) => (
                    <div key={item.pizza.id} className="flex justify-between items-center">
                      <div>
                        <p className="font-medium">{item.pizza.name}</p>
                        <p className="text-sm text-muted-foreground">
                          Quantity: {item.quantity} × ${item.pizza.price.toFixed(2)}
                        </p>
                      </div>
                      <p className="font-semibold">
                        ${(item.pizza.price * item.quantity).toFixed(2)}
                      </p>
                    </div>
                  ))}
                </div>

                <Separator className="my-4" />

                <div className="flex justify-between items-center text-lg font-bold">
                  <span>Total</span>
                  <span>${totalPrice.toFixed(2)}</span>
                </div>
              </CardContent>
            </Card>

            {/* Payment Section */}
            {clientSecret && orderId ? (
              <Card className="mt-6">
                <CardHeader>
                  <CardTitle>Payment Details</CardTitle>
                </CardHeader>
                <CardContent>
                  <Elements stripe={stripePromise} options={{ clientSecret }}>
                    <CheckoutForm orderId={orderId} onSuccess={handlePaymentSuccess} />
                  </Elements>
                </CardContent>
              </Card>
            ) : (
              <Card className="mt-6">
                <CardContent className="pt-6">
                  <Button
                    onClick={handlePlaceOrder}
                    className="w-full"
                    size="lg"
                    disabled={loading}
                  >
                    {loading ? (
                      <>
                        <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                        Creating order...
                      </>
                    ) : (
                      "Proceed to Payment"
                    )}
                  </Button>
                </CardContent>
              </Card>
            )}
          </div>

          {/* Delivery Info */}
          <div>
            <Card>
              <CardHeader>
                <CardTitle>Delivery Information</CardTitle>
              </CardHeader>
              <CardContent className="space-y-2">
                <p className="text-sm">
                  <span className="font-medium">Name:</span> {user.name}
                </p>
                <p className="text-sm">
                  <span className="font-medium">Email:</span> {user.email}
                </p>
                <Separator className="my-4" />
                <p className="text-xs text-muted-foreground">
                  Estimated delivery: 30-45 minutes
                </p>
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CheckoutPage;
