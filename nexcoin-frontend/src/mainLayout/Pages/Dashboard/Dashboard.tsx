import { useEffect, useState } from "react";
import { Coins, Send, Pickaxe } from "lucide-react";
import api from "../../../services/api";
import "./dashboardstyle.css";
import "../pagesstyle.css"

function Dashboard() {
  const [balance, setBalance] = useState(0);
  const [numberOfTransactions, setNumberOfTransactions] = useState(0);
  const [numberOfMinedBlocks, setNumberOfMinedBlocks] = useState(0);
  const [transactions, setTransactions] = useState<any[]>([]);

  async function getBalance() {
    const response = await api.get("/user/balance");
    setBalance(response.data);
  }
  useEffect(() => {
    getBalance();
  }, []);

  async function getNumberOfTransactions() {
    const response = await api.get("/user/transactions/count");
    setNumberOfTransactions(response.data);
  }
  useEffect(() => {
    getNumberOfTransactions();
  }, []);

  async function getNumberOfMinedBlocks() {
    const response = await api.get("/user/blocks/count");
    setNumberOfMinedBlocks(response.data);
  }
  useEffect(() => {
    getNumberOfMinedBlocks();
  }, []);

  async function getLatestTransactions() {
    const response = await api.get("/user/transactions/latest");

    setTransactions(response.data);
  }
  useEffect(() => {
    getLatestTransactions();
  }, []);

  return (
    <>
      <div className="container-options">
        <p className="option-title">Dashboard</p>
        <p className="option-subtitle">
          Welcome back! Here's your NexCoin overview.
        </p>
        <div className="container-cards">
          <div className="dash-small-cards">
            <div className="card-text-container">
              <p className="card-subtext-dash">Total Balance</p>
              <p className="card-text-dash">{balance} NXC</p>
            </div>
            <div className="card">
              <Coins size={35} color="#fff" />
            </div>
          </div>
          <div className="dash-small-cards">
            <div className="card-text-container">
              <p className="card-subtext-dash">Transactions</p>
              <p className="card-text-dash">{numberOfTransactions}</p>
            </div>
            <div className="card">
              <Send size={35} color="#fff" />
            </div>
          </div>
          <div className="dash-small-cards">
            <div className="card-text-container">
              <p className="card-subtext-dash">Blocks Mined</p>
              <p className="card-text-dash">{numberOfMinedBlocks}</p>
            </div>
            <div className="card">
              <Pickaxe size={35} color="#fff" />
            </div>
          </div>
        </div>
      </div>
      <div className="container-cards">
        <div className="big-card">
          <p className="card-text-dash">Recent Activity</p>
          {transactions.map((tx, i) => {
            const address = tx.direction === "receive" ? tx.from : tx.to;

            return (
              <div key={i} className="transaction-card">
                <div className="tx-left">
                  <p className="tx-type">
                    {tx.direction === "receive" ? "Received" : "Sent"}
                  </p>

                  <p className="tx-address">
                    {address?.slice(0, 6)}...{address?.slice(-4)}
                  </p>
                </div>

                <div className="tx-right">
                  <p className={`tx-amount ${tx.direction}`}>
                    {tx.direction === "receive" ? "+" : "-"}
                    {tx.amount} NXC
                  </p>

                  <p className="tx-time">{tx.status}</p>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </>
  );
}

export default Dashboard;
