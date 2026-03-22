import { useEffect, useState } from "react";
import { Pickaxe, Loader, CircleCheck } from "lucide-react";
import api from "../../../services/api";
import "../pagesstyle.css";
import "./minestyle.css";

function Dashboard() {
  const [numberOfMinedBlocks, setNumberOfMinedBlocks] = useState(0);
  const [status, setStatus] = useState("idle");
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  async function mineBlock() {
    setMessage("");
    setMessageType("");
    if (status !== "idle") return;

    try {
      setStatus("mining");

      await api.post("/blockchain/mine");

      await getNumberOfMinedBlocks();
      setStatus("mined");

      setTimeout(() => {
        setStatus("idle");
      }, 1500);
    } catch (error: any) {
      setStatus("idle");

      setMessage(
        error.response?.data?.message ||
          error.response?.data ||
          "Block mining error",
      );
      setMessageType("error");
    }
  }

  async function getNumberOfMinedBlocks() {
    const response = await api.get("/user/blocks/count");
    setNumberOfMinedBlocks(response.data);
  }
  useEffect(() => {
    getNumberOfMinedBlocks();
  }, []);

  return (
    <>
      <div className="container-options-centered">
        <p className="option-title">Mine Block</p>
        <p className="option-subtitle">
          Participate in the network and earn rewards
        </p>
        <div className="medium-card">
          <div className="container-options-centered">
            <div className="bigger-card-icon">
              <Pickaxe size={65} color="#fff" />
            </div>
            <p className="mine-text">Blocks Mined</p>
            <p className="mine-subtext">
              Click the button below to start mining a new block on the NexCoin
              blockchain
            </p>
            <button
              className={`mine-button ${status}`}
              type="button"
              onClick={mineBlock}
            >
              {status === "idle" && (
                <>
                  <Pickaxe size={18} color="#fff" />
                  Mine Block
                </>
              )}

              {status === "mining" && (
                <>
                  <Loader size={18} color="#fff" className="spin" />
                  Mining...
                </>
              )}

              {status === "mined" && (
                <>
                  <CircleCheck size={18} color="#fff" />
                  Mined!
                </>
              )}
            </button>
            {message && <p className={`message ${messageType}`}>{message}</p>}
          </div>
        </div>
        <div className="container-cards">
          <div className="square-small-cards">
            <div className="square-text-container">
              <p className="mine-subtext">Blocks Mined</p>
              <p className="mine-text"> {numberOfMinedBlocks} </p>
            </div>
          </div>
          <div className="square-small-cards">
            <div className="square-text-container">
              <p className="mine-subtext">Total Rewards</p>
              <p className="mine-text">
                {(numberOfMinedBlocks * 5.8).toFixed(2)} NXC
              </p>
            </div>
          </div>
          <div className="square-small-cards">
            <div className="square-text-container">
              <p className="mine-subtext">Reward Value</p>
              <p className="mine-text">5.75 NXC/block</p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default Dashboard;
