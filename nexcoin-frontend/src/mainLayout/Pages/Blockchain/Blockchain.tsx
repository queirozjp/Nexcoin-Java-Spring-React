import { useEffect, useState } from "react";
import { Blocks, Users, Hash } from "lucide-react";
import api from "../../../services/api";
import "./blockchainstyle.css";

function Blockchain() {
  const [chain, setChain] = useState<any[]>([]);
  const [openBlock, setOpenBlock] = useState<number | null>(null);

  async function getChain() {
    const response = await api.get("/blockchain/chain");
    setChain(response.data);
  }
  useEffect(() => {
    getChain();
  }, []);
  function toggleTransactions(id: number) {
    setOpenBlock(openBlock === id ? null : id);
  }

  return (
    <>
      <div className="container-options">
        <p className="option-title">Blockchain</p>
        <p className="option-subtitle">
          View all blocks and transactions on the NexCoin blockchains
        </p>
        <div className="container-cards">
          <div className="dash-small-cards">
            <div className="card-text-container">
              <p className="card-subtext-dash">Total Blocks</p>
              <p className="card-text-dash">20</p>
            </div>
            <div className="card">
              <Blocks size={35} color="#fff" />
            </div>
          </div>
          <div className="dash-small-cards">
            <div className="card-text-container">
              <p className="card-subtext-dash">Total Transactions</p>
              <p className="card-text-dash">200</p>
            </div>
            <div className="card">
              <Users size={35} color="#fff" />
            </div>
          </div>
          <div className="dash-small-cards">
            <div className="card-text-container">
              <p className="card-subtext-dash">Average Dificulty</p>
              <p className="card-text-dash">4.0</p>
            </div>
            <div className="card">
              <Hash size={35} color="#fff" />
            </div>
          </div>
        </div>
      </div>
      <div className="container-cards">
        {chain.map((block) => (
          <div className="big-card" key={block.id}>
            <div className="logo">
              <div className="card">
                <Blocks size={35} color="#fff" />
              </div>
              <div className="title">
                <p className="card-text-dash">Block #{block.id}</p>
              </div>
            </div>
            

            <p className="chain-text">
              <strong>Hash:</strong> {block.hash.slice(0, 25)}...
            </p>
            <p className="chain-text">
              <strong>Previous:</strong> {block.previousHash.slice(0, 25)}...
            </p>
            <p className="chain-text">
              <strong>Nonce:</strong> {block.nonce}
            </p>

            <button
              className="button-chain"
              onClick={() => toggleTransactions(block.id)}
            >
              {openBlock === block.id
                ? "Esconder transações"
                : `Mostrar transações (${block.transactions.length})`}
            </button>

            {openBlock === block.id && (
              <div className="transactions-chain">
                {block.transactions.map((tx: any) => (
                  <div key={tx.id} className="transaction-chain">
                    <p className="chain-text">
                      <strong>ID:</strong> {tx.id}
                    </p>
                    <p className="chain-text">
                      <strong>From:</strong> {tx.fromAddress ?? "Deposit/Mining Reward"}
                    </p>
                    <p className="chain-text">
                      <strong>To:</strong> {tx.toAddress}
                    </p>
                    <p className="chain-text">
                      <strong>Amount:</strong> {tx.coinAmount}
                    </p>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>
    </>
  );
}

export default Blockchain;
