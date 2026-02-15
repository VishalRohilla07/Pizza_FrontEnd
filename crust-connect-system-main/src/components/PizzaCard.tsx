import { Pizza } from "@/types";
import { useCart } from "@/context/CartContext";
import { Button } from "@/components/ui/button";
import { Plus, ShoppingCart } from "lucide-react";
import { Badge } from "@/components/ui/badge";

interface PizzaCardProps {
  pizza: Pizza;
}

const PizzaCard = ({ pizza }: PizzaCardProps) => {
  const { addToCart } = useCart();

  return (
    <div className="group animate-fade-in overflow-hidden rounded-xl border bg-card shadow-sm transition-all hover:shadow-lg">
      <div className="relative aspect-square overflow-hidden">
        <img
          src={pizza.imageUrl}
          alt={pizza.name}
          className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-110"
          loading="lazy"
        />
        {!pizza.available && (
          <div className="absolute inset-0 flex items-center justify-center bg-foreground/60">
            <span className="rounded-full bg-card px-4 py-1 text-sm font-semibold text-foreground">
              Sold Out
            </span>
          </div>
        )}
        <Badge
          className={`absolute left-3 top-3 ${
            pizza.category === "VEG"
              ? "bg-accent text-accent-foreground"
              : "bg-primary text-primary-foreground"
          }`}
        >
          {pizza.category === "VEG" ? "🌿 Veg" : "🍖 Non-Veg"}
        </Badge>
      </div>
      <div className="p-4">
        <div className="mb-2 flex items-start justify-between">
          <h3 className="font-display text-lg font-semibold text-foreground">{pizza.name}</h3>
          <span className="font-display text-lg font-bold text-primary">${pizza.price.toFixed(2)}</span>
        </div>
        <p className="mb-4 text-sm text-muted-foreground line-clamp-2">{pizza.description}</p>
        <Button
          onClick={() => addToCart(pizza)}
          disabled={!pizza.available}
          className="w-full gap-2"
          size="sm"
        >
          {pizza.available ? (
            <>
              <Plus className="h-4 w-4" />
              Add to Cart
            </>
          ) : (
            "Unavailable"
          )}
        </Button>
      </div>
    </div>
  );
};

export default PizzaCard;
