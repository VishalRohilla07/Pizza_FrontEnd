import HeroSection from "@/components/HeroSection";
import PizzaCard from "@/components/PizzaCard";
import { pizzas } from "@/data/pizzas";
import Navbar from "@/components/Navbar";
import { Pizza } from "lucide-react";

const Index = () => {
  const featured = pizzas.filter((p) => p.available).slice(0, 4);

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <HeroSection />

      <section className="container mx-auto px-4 py-16">
        <div className="mb-10 text-center">
          <h2 className="mb-2 font-display text-3xl font-bold text-foreground">Popular Picks</h2>
          <p className="text-muted-foreground">Our customers' favorite pizzas, crafted fresh daily</p>
        </div>
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {featured.map((pizza) => (
            <PizzaCard key={pizza.id} pizza={pizza} />
          ))}
        </div>
      </section>

      <footer className="border-t bg-card py-10">
        <div className="container mx-auto flex flex-col items-center gap-4 px-4 text-center">
          <div className="flex items-center gap-2">
            <Pizza className="h-6 w-6 text-primary" />
            <span className="font-display text-lg font-bold">PizzaHub</span>
          </div>
          <p className="text-sm text-muted-foreground">
            © 2026 PizzaHub. Crafted with love & mozzarella.
          </p>
        </div>
      </footer>
    </div>
  );
};

export default Index;
