import { useEffect, useState, useRef } from "react";
import { CircleCheck } from "lucide-react";
import { ec as EC } from "elliptic";
import SHA256 from "crypto-js/sha256";
import "../pagesstyle.css";
import api from "../../../services/api";

function Deposit() {
  const [fromAddress, setFromAddress] = useState<string>("");
  const [to, setTo] = useState<string>("");
  const inputAmount = useRef<HTMLInputElement>(null);
  const inputToAddresss = useRef<HTMLInputElement>(null);
  const inputPrivateKey = useRef<HTMLInputElement>(null);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [balance, setBalance] = useState(0);
  const [addedAmount, setAddedAmount] = useState(0);

  const ec = new EC("secp256k1");

  async function getBalance() {
    const response = await api.get("/user/balance");
    setBalance(response.data);
  }
  useEffect(() => {
    getBalance();
  }, []);

  async function createNewTransaction() {
    try {
      const rawValue = inputAmount.current?.value;
      const toAddress = inputToAddresss.current?.value || "";
      const privateKey = inputPrivateKey.current?.value || "";

      if (!privateKey) {
        setMessage("Private key is required");
        setMessageType("error");
        return;
      }

      const key = ec.keyFromPrivate(privateKey, "hex");

      setTo(toAddress);

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

      const formattedAmount = amount.toFixed(2);
      const data = fromAddress + toAddress + formattedAmount;

      console.log("Frontend data:", data);

      const hash = SHA256(data).toString();
      const sig = key.sign(hash, "hex").toDER();

      const signature = btoa(String.fromCharCode(...new Uint8Array(sig)));

      const response = await api.post("/transactions/newtransaction", {
        toAddress,
        amount: formattedAmount,
        signature,
      });

      setMessage(response.data.message);
      setMessageType("success");
    } catch (error: any) {
      console.error(error);

      setMessage(error.response?.data?.message || "Deposit failed");
      setMessageType("error");
    }
  }

  async function getComplexDetails() {
    const response = await api.get("/user/complexdetails");
    setFromAddress(response.data.message);
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

  if (messageType === "success") {
    return (
      <div className="success-container">
        <div className="success-icon">
          <CircleCheck size={60} color="green" />
        </div>

        <h1>Funds Added Successfully!</h1>
        <p>Your wallet has been credited</p>

        <div className="success-card">
          <div>
            <span className="label">From Address</span>
            <p>{fromAddress}</p>
          </div>

          <div>
            <span className="label">To Address</span>
            <p>{to}</p>
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
        {message && <p className={`message ${messageType}`}>{message}</p>}
        <form>
          <p className="transactions-p">To Address</p>
          <input
            name="amount"
            placeholder="Ex: 0x7a3f...9c21"
            step="0.01"
            ref={inputToAddresss}
          />
          <p className="transactions-p">Amount (NXC)</p>
          <input
            name="amount"
            type="number"
            placeholder="0.00NXC"
            step="0.01"
            ref={inputAmount}
          />
          <p className="transactions-p">Sign with your Private Key</p>
          <input
            name="amount"
            placeholder="Ex: MFkwEw...Pg=="
            step="0.01"
            ref={inputPrivateKey}
          />
          <p className="transactions-p2">Amount to add to your wallet</p>
          <div className="transactions-small-cards">
            <p className="transactions-p2">Your Wallet Address</p>
            <p className="transactions-p2">{fromAddress}</p>
          </div>
          <button type="button" onClick={createNewTransaction}>
            Send Transaction
          </button>
        </form>
      </div>
    </div>
  );
}

export default Deposit;
