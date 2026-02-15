import React, { createContext, useContext, useState, useEffect } from "react";
import { User, UserRole } from "@/types";
import { toast } from "@/hooks/use-toast";
import * as authApi from "@/api/auth";

interface AuthContextType {
  user: User | null;
  login: (email: string, password: string) => Promise<boolean>;
  register: (name: string, email: string, password: string) => Promise<boolean>;
  logout: () => void;
  isAdmin: boolean;
  loading: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  // Load user from localStorage on mount
  useEffect(() => {
    const storedUser = authApi.getStoredUser();
    if (storedUser) {
      setUser(storedUser);
    }
    setLoading(false);
  }, []);

  const login = async (email: string, password: string): Promise<boolean> => {
    try {
      const response = await authApi.login({ email, password });

      const userData: User = {
        id: response.id.toString(),
        name: response.name,
        email: response.email,
        role: response.role as UserRole,
      };

      authApi.storeAuth(response);
      setUser(userData);

      toast({ title: "Welcome back!", description: `Logged in as ${response.name}` });
      return true;
    } catch (error: any) {
      toast({
        title: "Login failed",
        description: error.message || "Invalid credentials.",
        variant: "destructive"
      });
      return false;
    }
  };

  const register = async (name: string, email: string, password: string): Promise<boolean> => {
    try {
      const response = await authApi.register({ name, email, password });

      const userData: User = {
        id: response.id.toString(),
        name: response.name,
        email: response.email,
        role: response.role as UserRole,
      };

      authApi.storeAuth(response);
      setUser(userData);

      toast({ title: "Welcome!", description: "Account created successfully." });
      return true;
    } catch (error: any) {
      toast({
        title: "Registration failed",
        description: error.message || "Could not create account.",
        variant: "destructive"
      });
      return false;
    }
  };

  const logout = () => {
    authApi.clearAuth();
    setUser(null);
    toast({ title: "Logged out", description: "See you next time!" });
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout, isAdmin: user?.role === "ADMIN", loading }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};
