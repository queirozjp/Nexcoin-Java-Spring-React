import { createRoot } from "react-dom/client";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import "./index.css";
import ProtectedRoute from "./ProtectedRoute";
import Register from "./authLayout/Register/Register";
import Login from "./authLayout/Login/Login";
import Home from "./authLayout/Home/Home";
import MainLayout from "./mainLayout/MainLayout/MainLayout";
import Dashboard from "./mainLayout/Pages/Dashboard/Dashboard";
import Wallet from "./mainLayout/Pages/Wallet/Wallet";
import Balance from "./mainLayout/Pages/Balance/Balance";
import Deposit from "./mainLayout/Pages/Deposit/Deposit";
import Mine from "./mainLayout/Pages/Mine/Mine";
import Blockchain from "./mainLayout/Pages/Blockchain/Blockchain";
import NewTransaction from "./mainLayout/Pages/NewTransaction/NewTransaction";

createRoot(document.getElementById("root")!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<Navigate to="/auth/home" />} />
      <Route path="/auth/home" element={<Home />} />
      <Route path="/auth/register" element={<Register />} />
      <Route path="/auth/login" element={<Login />} />
      <Route
        path="/main"
        element={
          <ProtectedRoute>
            <MainLayout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="dashboard" />} />
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="wallet" element={<Wallet />} />
        <Route path="balance" element={<Balance />} />
        <Route path="deposit" element={<Deposit />} />
        <Route path="mine" element={<Mine />} />
        <Route path="blockchain" element={<Blockchain />} />
        <Route path="newtransaction" element={<NewTransaction />} />
      </Route>
      <Route path="*" element={<Navigate to="/auth/home" />} />
    </Routes>
  </BrowserRouter>,
);
