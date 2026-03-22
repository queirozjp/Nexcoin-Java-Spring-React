import { useEffect, useState, useRef } from "react";
import { useNavigate } from "react-router-dom"
import { CircleCheck } from "lucide-react";
import "./deposit.css";
import "../pagesstyle.css";
import api from "../../../services/api";

function Deposit() {
  const [address, setAddress] = useState<string>("");
  const inputAmount = useRef<HTMLInputElement>(null);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [balance, setBalance] = useState(0);
  const [addedAmount, setAddedAmount] = useState(0);

  async function getBalance() {
    const response = await api.get("/user/balance");
    setBalance(response.data);
  }
  useEffect(() => {
    getBalance();
  }, []);

  async function addFunds() {
    const rawValue = inputAmount.current?.value;

    if (!rawValue) {
      setMessage("Enter an amount");
      setMessageType("error");
      return;
    }

    const amount = Number(rawValue.replace(",", "."));
    setAddedAmount(amount);

    if (isNaN(amount)) {
      setMessage("Invalid amount");
      setMessageType("error");
      return;
    }

    try {
      const response = await api.post("/transactions/deposit", {
        amount,
      });
      setMessage(response.data.message);
      setMessageType("success");
    } catch (error: any) {
      const message = error.response?.data?.message;
      setMessage(message || "Deposit failed");
      setMessageType("error");
    }
  }

  async function getComplexDetails() {
    const response = await api.get("/user/complexdetails");
    setAddress(response.data.message);
  }
  useEffect(() => {
    getComplexDetails();
  }, []);

  useEffect(() => {
  if (messageType !== "success") return;
  getBalance();
  const timer = setTimeout(() => {
    setMessageType("");
    setMessage("");
  }, 3000);

  return () => clearTimeout(timer);
}, [messageType]);
  
  if (messageType === "success"){
    return(
      <div className="success-container">

      <div className="success-icon">
        <CircleCheck size={60} color="green"/>
      </div>

      <h1>Funds Added Successfully!</h1>
      <p>Your wallet has been credited</p>

      <div className="success-card">

        <div>
          <span className="label">From Address</span>
          <p>{address}</p>
        </div>

        <div>
          <span className="label">Amount Added</span>
          <p className="amount">+{addedAmount} NXC</p>
        </div>

        <div>
          <span className="label">New Balance</span>
          <p>{balance} NXC</p>
        </div>

      </div>

    </div>
    );
  }

  return (
    <div className="container-options-centered">
      <p className="option-title">Deposit</p>
      <p className="option-subtitle">Deposit NexCoin to your wallet.</p>
      <div className="medium-card">
        <form>
          <p className="transactions-p">Amount (NXC)</p>
          <input
            name="amount"
            type="number"
            placeholder="0.00NXC"
            step="0.01"
            ref={inputAmount}
          />
          <p className="transactions-p2">Amount to add to your wallet</p>
          <div className="transactions-small-cards">
            <p className="transactions-p2">Your Wallet Address</p>
            <p className="transactions-p2">{address}</p>
          </div>
          <button type="button" onClick={addFunds}>
            Deposit
          </button>
        </form>
      </div>
    </div>
  );
}

export default Deposit;
