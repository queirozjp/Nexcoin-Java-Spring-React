import { useEffect, useState } from "react";
import api from "../../../services/api";
import "./balancestyle.css";

function Balance() {
  const [balance, setBalance] = useState(0);
  const [address, setAddress] = useState<string>("");
  const [transactions, setTransactions] = useState<any[]>([]);
  const [page, setPage] = useState(0);
  const size = 5;

  async function getComplexDetails() {
    const response = await api.get("/user/complexdetails");
    setAddress(response.data.message);
  }
  useEffect(() => {
    getComplexDetails();
  }, []);

  async function getBalance() {
    const response = await api.get("/user/balance");
    setBalance(response.data);
  }
  useEffect(() => {
    getBalance();
  }, []);

  async function getTransactions(pageNumber = 0) {
    const response = await api.get(
      `/user/transactions/history?page=${pageNumber}&size=${size}`,
    );
    if (pageNumber === 0) {
      setTransactions(response.data);
    } else {
      setTransactions((prev) => [...prev, ...response.data]);
    }
  }
  useEffect(() => {
    getTransactions(0);
  }, []);

  function loadMore() {
    const nextPage = page + 1;
    setPage(nextPage);
    getTransactions(nextPage);
  }

  return (
    <>
      <div className="container-options">
        <p className="option-title">Balance</p>
        <p className="option-subtitle">
          View your balance and transaction history
        </p>
        <div className="container-cards">
          <div className="big-card-balance">
            <p className="balance-p">Current Balance</p>
            <p className="balance-h">{balance} NXC</p>
            <p className="balance-p">Wallet Address: {address}</p>
          </div>
          <div className="big-card">
            <p className="card-text-dash">History</p>
            <div className="history-table">
              <div className="table-header">
                <span>Type</span>
                <span>Address</span>
                <span>Amount</span>
                <span>Date</span>
                <span>Status</span>
              </div>
              {transactions.map((tx, index) => (
                <div className="table-row" key={index}>
                  <div className="type">
                    <div
                      className={`icon ${tx.direction === "receive" ? "received" : "sent"}`}
                    >
                      {tx.direction === "receive" ? "↙" : "↗"}
                    </div>

                    <span>
                      {tx.direction === "receive" ? "Received" : "Sent"}
                    </span>
                  </div>

                  <span>{tx.direction === "receive" ? tx.from : tx.to}</span>

                  <span
                    className={`amount ${tx.direction === "receive" ? "positive" : "negative"}`}
                  >
                    {tx.direction === "receive" ? "+" : "-"}
                    {tx.amount} NXC
                  </span>

                  <span>{new Date(tx.time).toLocaleString()}</span>

                  <span className="hash">{tx.status}</span>
                </div>
              ))}
            </div>
            <button className="load-more" onClick={loadMore}>
              Show more
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
export default Balance;
