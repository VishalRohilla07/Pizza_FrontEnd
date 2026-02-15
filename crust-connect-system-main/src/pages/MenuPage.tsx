import { useState, useEffect } from "react";
import { useCart } from "@/context/CartContext";
import { useAuth } from "@/context/AuthContext";
import { Pizza } from "@/types";
import Navbar from "@/components/Navbar";
import PizzaCard from "@/components/PizzaCard";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import * as pizzaApi from "@/api/pizza";
import { Loader2 } from "lucide-react";

const MenuPage = () => {
  const [pizzas, setPizzas] = useState<Pizza[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<"ALL" | "VEG" | "NON_VEG">("ALL");
  const { addToCart } = useCart();
  const { user } = useAuth();

  // Fetch pizzas from backend
  useEffect(() => {
    const fetchPizzas = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await pizzaApi.getAllPizzas();
        setPizzas(data);
      } catch (err: any) {
        setError(err.message || "Failed to load pizzas");
      } finally {
        setLoading(false);
      }
    };

    fetchPizzas();
  }, []);

  const filteredPizzas = pizzas.filter((p) => {
    if (filter === "ALL") return true;
    return p.category === filter;
  });

  if (loading) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="container mx-auto flex items-center justify-center px-4 py-16">
          <div className="flex flex-col items-center gap-4">
            <Loader2 className="h-12 w-12 animate-spin text-primary" />
            <p className="text-muted-foreground">Loading delicious pizzas...</p>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="container mx-auto flex items-center justify-center px-4 py-16">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-destructive">Error Loading Menu</h2>
            <p className="mt-2 text-muted-foreground">{error}</p>
            <Button onClick={() => window.location.reload()} className="mt-4">
              Try Again
            </Button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <div className="container mx-auto px-4 py-8">
        <div className="mb-8 text-center">
          <h1 className="font-display text-4xl font-bold">Our Menu</h1>
          <p className="mt-2 text-muted-foreground">Handcrafted pizzas made with love</p>
        </div>

        <Tabs value={filter} onValueChange={(v) => setFilter(v as typeof filter)} className="mb-8">
          <TabsList className="grid w-full max-w-md mx-auto grid-cols-3">
            <TabsTrigger value="ALL">All Pizzas</TabsTrigger>
            <TabsTrigger value="VEG">Vegetarian</TabsTrigger>
            <TabsTrigger value="NON_VEG">Non-Veg</TabsTrigger>
          </TabsList>
        </Tabs>

        {filteredPizzas.length === 0 ? (
          <div className="text-center py-16">
            <p className="text-muted-foreground">No pizzas available in this category.</p>
          </div>
        ) : (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {filteredPizzas.map((pizza) => (
              <PizzaCard key={pizza.id} pizza={pizza} onAddToCart={addToCart} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MenuPage;
