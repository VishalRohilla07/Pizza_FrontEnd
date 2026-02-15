import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { ArrowRight } from "lucide-react";
import heroPizza from "@/assets/hero-pizza.jpg";

const HeroSection = () => {
  return (
    <section className="relative overflow-hidden">
      <div className="absolute inset-0">
        <img src={heroPizza} alt="Delicious pizza" className="h-full w-full object-cover" />
        <div className="absolute inset-0 bg-gradient-to-r from-foreground/80 via-foreground/60 to-transparent" />
      </div>
      <div className="container relative mx-auto px-4 py-24 md:py-36">
        <div className="max-w-xl">
          <h1 className="mb-4 font-display text-5xl font-extrabold leading-tight text-primary-foreground md:text-6xl">
            Crafted with <span className="text-cheese">Passion</span>, Baked to Perfection
          </h1>
          <p className="mb-8 text-lg text-primary-foreground/80">
            Hand-tossed dough, premium ingredients, and a love for pizza that you can taste in every bite. Order your favorite now.
          </p>
          <div className="flex gap-4">
            <Link to="/menu">
              <Button size="lg" className="gap-2 bg-primary text-primary-foreground hover:bg-primary/90">
                Order Now
                <ArrowRight className="h-4 w-4" />
              </Button>
            </Link>
            <Link to="/menu">
              <Button size="lg" variant="outline" className="border-cheese bg-cheese text-foreground hover:bg-cheese/80">
                View Menu
              </Button>
            </Link>
          </div>
        </div>
      </div>
    </section>
  );
};

export default HeroSection;
