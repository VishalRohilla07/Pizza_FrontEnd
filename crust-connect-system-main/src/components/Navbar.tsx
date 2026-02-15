import { Link, useLocation } from "react-router-dom";
import { ShoppingCart, User, Pizza, LogOut, LayoutDashboard } from "lucide-react";
import { useCart } from "@/context/CartContext";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/button";

const Navbar = () => {
  const { totalItems } = useCart();
  const { user, logout, isAdmin } = useAuth();
  const location = useLocation();

  return (
    <nav className="sticky top-0 z-50 border-b bg-card/80 backdrop-blur-md">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        <Link to="/" className="flex items-center gap-2">
          <Pizza className="h-7 w-7 text-primary" />
          <span className="font-display text-xl font-bold text-foreground">PizzaHub</span>
        </Link>

        <div className="hidden items-center gap-6 md:flex">
          <Link
            to="/menu"
            className={`text-sm font-medium transition-colors hover:text-primary ${location.pathname === "/menu" ? "text-primary" : "text-muted-foreground"}`}
          >
            Menu
          </Link>
          {user && (
            <Link
              to="/orders"
              className={`text-sm font-medium transition-colors hover:text-primary ${location.pathname === "/orders" ? "text-primary" : "text-muted-foreground"}`}
            >
              My Orders
            </Link>
          )}
          {isAdmin && (
            <Link
              to="/admin"
              className={`text-sm font-medium transition-colors hover:text-primary ${location.pathname.startsWith("/admin") ? "text-primary" : "text-muted-foreground"}`}
            >
              Admin
            </Link>
          )}
        </div>

        <div className="flex items-center gap-3">
          <Link to="/cart" className="relative">
            <Button variant="ghost" size="icon" className="relative">
              <ShoppingCart className="h-5 w-5" />
              {totalItems > 0 && (
                <span className="absolute -right-1 -top-1 flex h-5 w-5 items-center justify-center rounded-full bg-primary text-[10px] font-bold text-primary-foreground">
                  {totalItems}
                </span>
              )}
            </Button>
          </Link>

          {user ? (
            <div className="flex items-center gap-2">
              {isAdmin && (
                <Link to="/admin">
                  <Button variant="ghost" size="icon">
                    <LayoutDashboard className="h-5 w-5" />
                  </Button>
                </Link>
              )}
              <Button variant="ghost" size="sm" onClick={logout} className="gap-2 text-muted-foreground">
                <LogOut className="h-4 w-4" />
                <span className="hidden sm:inline">{user.name}</span>
              </Button>
            </div>
          ) : (
            <Link to="/login">
              <Button variant="default" size="sm" className="gap-2">
                <User className="h-4 w-4" />
                Login
              </Button>
            </Link>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
