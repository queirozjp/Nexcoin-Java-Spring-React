import { Coins, Send, Pickaxe, BarChart3 } from "lucide-react";
import { Link } from "react-router-dom";
import './homestyle.css'

function Home() {
  
  return (
    <>
    <div className="container-home">
        <div className="logo">
            <div className="card">
                <Coins size={35} color="#fff" />
                </div>
                <div className='title'>Nexcoin</div>
            </div>
            <div className='subtitle'>Secure. Decentralized. Transparent.</div>
        <div className='button-container'>
            <Link to="/auth/login" className="login-button">Login</Link>
            <Link to="/auth/register" className="register-button">register</Link>
        </div>
    </div>
    <div className='card-container'>
        <div className='card-home'>
            <Send size={35} color="#fff" />
            <div className='subtitle'>Send</div>
            <div className='card-text'>Transfer NexCoin to anyone, anywhere in the world instantly with our secure blockchain network.</div>
        </div>
        <div className='card-home'>
            <Pickaxe size={35} color="#fff" />
            <div className='subtitle'>Mine</div>
            <div className='card-text'>Participate in the network by mining blocks and earn rewards for securing the blockchain.</div>
        </div>
        <div className='card-home'>
            <BarChart3 size={35} color="#fff" />
            <div className='subtitle'>Track Balance</div>
            <div className='card-text'>Monitor your transactions and balance in real-time with our comprehensive dashboard.</div>
        </div>
    </div>
    </>
  )
}

export default Home
