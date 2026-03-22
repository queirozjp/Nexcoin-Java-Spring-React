import { useState, useRef } from "react";
import { Coins } from "lucide-react";
import "./registerstyle.css";
import "../authstyle.css"
import api from "../../services/api.ts";

function Register() {
  const inputName = useRef<HTMLInputElement>(null);
  const inputPassword = useRef<HTMLInputElement>(null);
  const inputEmail = useRef<HTMLInputElement>(null);

  const [message, setMessage] = useState("");
  const [privateKey, setPrivateKey] = useState("");
  const [messageType, setMessageType] = useState("");

  async function registerUser() {
    const username = inputName.current?.value;
    const email = inputEmail.current?.value;
    const password = inputPassword.current?.value;

    if (!password || password.length < 8) {
      setMessage("Password must have at least 8 characters");
      setMessageType("error");
      return;
    }
    try {
      const response = await api.post("/auth/register", {
        username,
        email,
        password,
      });

      setMessage(response.data.message);
      setPrivateKey(response.data.privateKey);
      setMessageType("success");
    } catch (error: any) {
      setMessage(error.response?.data?.message || "Error creating account")
      setMessageType("error");
    }
  }

  return (
    <>
      <div>
        <div className="container-auth">
          <form>
            <div className="logo">
              <div className="card">
                <Coins size={35} color="#fff" />
              </div>
              <h1>Nexcoin</h1>
            </div>

            <h2>Create Your Account</h2>
            <input
              name="name"
              type="text"
              placeholder="Choose a username"
              ref={inputName}
            />

            <input
              name="email"
              type="email"
              placeholder="Enter your email"
              ref={inputEmail}
            />

            <input
              name="password"
              type="password"
              placeholder="Create a password"
              ref={inputPassword}
            />
            <button type="button" onClick={registerUser}>
              Register
            </button>
            {message && <p className={`message ${messageType}`}>{message}</p>}

            {privateKey && (
              <div className="private-key-box">
                <p className="private-key-title">⚠ Save your private key</p>

                <div className="private-key">{privateKey}</div>

                <button
                  type="button"
                  className="copy-button"
                  onClick={() => navigator.clipboard.writeText(privateKey)}
                >
                  Copy Key
                </button>
              </div>
            )}
            <p className="auth-alternate">
              Already have an account? <a href="/auth/login">Login</a>
            </p>
          </form>
        </div>
      </div>
    </>
  );
}

export default Register;
