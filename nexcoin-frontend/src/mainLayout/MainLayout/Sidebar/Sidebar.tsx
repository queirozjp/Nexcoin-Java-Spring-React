import { NavLink, useNavigate } from "react-router-dom";
import {
  LayoutDashboard,
  Wallet,
  Coins,
  Send,
  Plus,
  Pickaxe,
  Boxes,
} from "lucide-react";
import "./sidebarstyle.css";

function Sidebar() {
  const navigate = useNavigate();
  async function logout() {
    localStorage.removeItem("token");
    navigate("/auth/login");
  }

  return (
    <div className="sidebar">
      <div className="sidebar-logo">
        <div className="logo">
          <div className="card">
            <Coins size={35} color="#fff" />
          </div>
          <h1>Nexcoin</h1>
        </div>
      </div>

      <nav className="sidebar-menu">
        <NavLink
          to="/main/dashboard"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <LayoutDashboard size={20} />
          Dashboard
        </NavLink>

        <NavLink
          to="/main/wallet"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <Wallet size={20} />
          My Wallet
        </NavLink>

        <NavLink
          to="/main/balance"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <Coins size={20} />
          Balance
        </NavLink>

        <NavLink
          to="/main/newtransaction"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <Send size={20} />
          New Transaction
        </NavLink>

        <NavLink
          to="/main/deposit"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <Plus size={20} />
          Deposit
        </NavLink>

        <NavLink
          to="/main/mine"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <Pickaxe size={20} />
          Mine Block
        </NavLink>

        <NavLink
          to="/main/blockchain"
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <Boxes size={20} />
          Blockchain
        </NavLink>
      </nav>

      <div className="sidebar-footer">
        <button className="logout-btn" onClick={logout}>Logout</button>
      </div>
    </div>
  );
}

export default Sidebar;
