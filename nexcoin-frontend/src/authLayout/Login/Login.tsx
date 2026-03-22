import { useState, useRef } from "react";
import { useNavigate } from "react-router-dom"
import { Coins} from "lucide-react";
import "../authstyle.css"
import api from "../../services/api";


function Login() {
  
  const inputEmail = useRef<HTMLInputElement>(null);
  const inputPassword = useRef<HTMLInputElement>(null);

  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");

  const navigate = useNavigate()

  async function loginUser(){
    const email = inputEmail.current?.value
    const password = inputPassword.current?.value 
    
    try{
        const response = await api.post("/auth/login", {
        email, 
        password
      })
      const token = response.data.token

      setMessage(response.data.message);
      setMessageType("success")
      localStorage.setItem("token", token)
      navigate("/main/dashboard")
    } catch(error: any){
      const message = error.response?.data?.message
      setMessage(message || "Login failed")
      setMessageType("error")
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
          
          <h2>Enter Your Account</h2>
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
          <button type="button" onClick={loginUser}>
              Login
          </button>
          {message && <p className={`message ${messageType}`}>{message}</p>}
          <p className="auth-alternate">
              Does'nt have an account? <a href="/auth/register">Register</a>
          </p>
        </form>
      </div>
    </div>
    </>
  )
}

export default Login
