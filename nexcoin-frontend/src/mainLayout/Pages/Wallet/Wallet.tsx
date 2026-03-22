import { useEffect, useState } from "react";
import {
  Wallet as WalletIcon,
  MapPin,
  Key,
  Copy,
  CopyCheck,
} from "lucide-react";
import api from "../../../services/api";
import "./walletstyle.css";

function Wallet() {
  const [username, setUsername] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [address, setAddress] = useState<string>("");
  const [publicKey, setPublicKey] = useState<string>("");

  async function getUser() {
    const response = await api.get("/user/simpledetails");
    setEmail(response.data.message);
    setUsername(response.data.data);
  }
  useEffect(() => {
    getUser();
  }, []);

  async function getComplexDetails() {
    const response = await api.get("/user/complexdetails");
    setAddress(response.data.message);
    setPublicKey(response.data.data);
  }
  useEffect(() => {
    getComplexDetails();
  }, []);

  const [copiedAddress, setCopiedAddress] = useState(false);

  const handleCopyAddress = () => {
    navigator.clipboard.writeText(address);

    setCopiedAddress(true);

    setTimeout(() => {
      setCopiedAddress(false);
    }, 2000);
  };

  const [copiedKey, setCopiedKey] = useState(false);

  const handleCopyKey = () => {
    navigator.clipboard.writeText(publicKey);

    setCopiedKey(true);

    setTimeout(() => {
      setCopiedKey(false);
    }, 2000);
  };

  return (
    <>
      <div className="container-options">
        <p className="option-title">My Wallet</p>
        <p className="option-subtitle">
          View your wallet credentials and public information
        </p>
        <div className="container-cards">
          <div className="big-card">
            <div className="wallet-user">
              <div className="bigger-card-icon">
                <WalletIcon size={35} color="#fff" />
              </div>
              <div className="wallet-text-container">
                <span className="wallet-username">{username}</span>
                <span className="wallet-p">{email}</span>
              </div>
            </div>
            <div className="wallet-details-container">
                <span className="wallet-p">
              <MapPin size={14} color="#94a3b8" /> Wallet Address
            </span>
              <div className="wallet-details-card">
                <h2>{address}</h2>
                <button
                  type="button"
                  className="copy-button"
                  onClick={handleCopyAddress}
                >
                  {copiedAddress ? (
                    <CopyCheck size={18} color="#4ade80" />
                  ) : (
                    <Copy size={18} color="#fff" />
                  )}
                </button>
              </div>
              <p className="wallet-p2">This is your public wallet address. Share this to receive NexCoin payments.</p>
              <div className="wallet-details-container">
                <span className="wallet-p">
                <Key size={14} color="#94a3b8" /> Public Key
              </span>
                <div className="wallet-details-card">
                  <h2>{publicKey.slice(0, 6)}...{publicKey.slice(-4)}</h2>
                  <button
                    type="button"
                    className="copy-button"
                    onClick={handleCopyKey}
                  >
                    {copiedKey ? (
                      <CopyCheck size={18} color="#4ade80" />
                    ) : (
                      <Copy size={18} color="#fff" />
                    )}
                  </button>
                </div>
                <p className="wallet-p2">Your public key is used to verify transactions and can be safely shared.</p>
              </div>
            </div>
          </div>
        </div>
        <div className="security-notice">
      <div className="security-icon">
        <Key size={28} />
      </div>

      <div className="security-content">
        <h2>Security Notice</h2>
        <p>
          Never share your private key with anyone. Your public key and wallet
          address are safe to share, but your private key grants full access to
          your funds. NexCoin will never ask for your private key.
        </p>
      </div>
    </div>
      </div>
      
    </>
  );
}

export default Wallet;
