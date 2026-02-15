import { useState } from "react";
import Navbar from "@/components/Navbar";
import { pizzas as initialPizzas } from "@/data/pizzas";
import { sampleOrders } from "@/data/orders";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Pizza as PizzaIcon, ShoppingBag, Users, DollarSign, Edit, Trash2 } from "lucide-react";
import { OrderStatus } from "@/types";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { toast } from "@/hooks/use-toast";

const AdminPage = () => {
  const [orders, setOrders] = useState(sampleOrders);

  const updateOrderStatus = (orderId: string, status: OrderStatus) => {
    setOrders((prev) =>
      prev.map((o) => (o.id === orderId ? { ...o, orderStatus: status } : o))
    );
    toast({ title: "Status updated", description: `Order ${orderId} → ${status}` });
  };

  const stats = [
    { label: "Total Pizzas", value: initialPizzas.length, icon: <PizzaIcon className="h-5 w-5" /> },
    { label: "Total Orders", value: orders.length, icon: <ShoppingBag className="h-5 w-5" /> },
    { label: "Revenue", value: `$${orders.reduce((s, o) => s + o.totalAmount, 0).toFixed(2)}`, icon: <DollarSign className="h-5 w-5" /> },
    { label: "Customers", value: 2, icon: <Users className="h-5 w-5" /> },
  ];

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <div className="container mx-auto px-4 py-10">
        <h1 className="mb-8 font-display text-3xl font-bold">Admin Dashboard</h1>

        <div className="mb-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {stats.map((s) => (
            <div key={s.label} className="flex items-center gap-4 rounded-xl border bg-card p-5">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary/10 text-primary">
                {s.icon}
              </div>
              <div>
                <p className="text-sm text-muted-foreground">{s.label}</p>
                <p className="font-display text-2xl font-bold">{s.value}</p>
              </div>
            </div>
          ))}
        </div>

        <Tabs defaultValue="orders">
          <TabsList>
            <TabsTrigger value="orders">Orders</TabsTrigger>
            <TabsTrigger value="pizzas">Pizzas</TabsTrigger>
          </TabsList>

          <TabsContent value="orders" className="mt-6">
            <div className="space-y-4">
              {orders.map((order) => (
                <div key={order.id} className="flex flex-wrap items-center justify-between gap-4 rounded-xl border bg-card p-4">
                  <div>
                    <p className="font-display font-bold">{order.id}</p>
                    <p className="text-sm text-muted-foreground">
                      {order.items.map((i) => `${i.quantity}x ${i.pizza.name}`).join(", ")}
                    </p>
                  </div>
                  <div className="flex items-center gap-3">
                    <span className="font-bold text-primary">${order.totalAmount.toFixed(2)}</span>
                    <Select
                      value={order.orderStatus}
                      onValueChange={(v) => updateOrderStatus(order.id, v as OrderStatus)}
                    >
                      <SelectTrigger className="w-44">
                        <SelectValue />
                      </SelectTrigger>
                      <SelectContent>
                        {["PLACED", "CONFIRMED", "PREPARING", "OUT_FOR_DELIVERY", "DELIVERED", "CANCELLED"].map((s) => (
                          <SelectItem key={s} value={s}>{s}</SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                </div>
              ))}
            </div>
          </TabsContent>

          <TabsContent value="pizzas" className="mt-6">
            <div className="space-y-3">
              {initialPizzas.map((pizza) => (
                <div key={pizza.id} className="flex items-center gap-4 rounded-xl border bg-card p-4">
                  <img src={pizza.imageUrl} alt={pizza.name} className="h-14 w-14 rounded-lg object-cover" />
                  <div className="flex-1">
                    <p className="font-display font-semibold">{pizza.name}</p>
                    <p className="text-sm text-muted-foreground">${pizza.price.toFixed(2)}</p>
                  </div>
                  <Badge variant={pizza.available ? "default" : "secondary"}>
                    {pizza.available ? "Available" : "Sold Out"}
                  </Badge>
                  <div className="flex gap-1">
                    <Button variant="ghost" size="icon"><Edit className="h-4 w-4" /></Button>
                    <Button variant="ghost" size="icon"><Trash2 className="h-4 w-4 text-destructive" /></Button>
                  </div>
                </div>
              ))}
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
};

export default AdminPage;
