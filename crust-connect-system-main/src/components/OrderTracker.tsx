import { OrderStatus } from "@/types";
import { Check, Clock, ChefHat, Truck, Package, XCircle } from "lucide-react";

const statusSteps: { status: OrderStatus; label: string; icon: React.ReactNode }[] = [
  { status: "PLACED", label: "Placed", icon: <Clock className="h-5 w-5" /> },
  { status: "CONFIRMED", label: "Confirmed", icon: <Check className="h-5 w-5" /> },
  { status: "PREPARING", label: "Preparing", icon: <ChefHat className="h-5 w-5" /> },
  { status: "OUT_FOR_DELIVERY", label: "On the way", icon: <Truck className="h-5 w-5" /> },
  { status: "DELIVERED", label: "Delivered", icon: <Package className="h-5 w-5" /> },
];

const statusOrder: OrderStatus[] = ["PLACED", "CONFIRMED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED"];

interface OrderTrackerProps {
  currentStatus: OrderStatus;
}

const OrderTracker = ({ currentStatus }: OrderTrackerProps) => {
  if (currentStatus === "CANCELLED") {
    return (
      <div className="flex items-center gap-2 rounded-lg bg-destructive/10 p-4 text-destructive">
        <XCircle className="h-5 w-5" />
        <span className="font-medium">Order Cancelled</span>
      </div>
    );
  }

  const currentIndex = statusOrder.indexOf(currentStatus);

  return (
    <div className="flex items-center justify-between">
      {statusSteps.map((step, index) => {
        const isActive = index <= currentIndex;
        const isCurrent = index === currentIndex;
        return (
          <div key={step.status} className="flex flex-1 items-center">
            <div className="flex flex-col items-center gap-1">
              <div
                className={`flex h-10 w-10 items-center justify-center rounded-full transition-all ${
                  isActive
                    ? "bg-primary text-primary-foreground"
                    : "bg-muted text-muted-foreground"
                } ${isCurrent ? "ring-4 ring-primary/20" : ""}`}
              >
                {step.icon}
              </div>
              <span className={`text-xs font-medium ${isActive ? "text-foreground" : "text-muted-foreground"}`}>
                {step.label}
              </span>
            </div>
            {index < statusSteps.length - 1 && (
              <div className={`mx-1 h-0.5 flex-1 ${index < currentIndex ? "bg-primary" : "bg-muted"}`} />
            )}
          </div>
        );
      })}
    </div>
  );
};

export default OrderTracker;
