import { useEffect, useState } from "react";
import api from "../../../services/api"
import "./headerstyle.css"

function Header() {

  const [username, setUsername] = useState<string>("")
  const [email, setEmail] = useState<string>("")

  async function getUser(){
    const response = await api.get("/user/simpledetails")
    setEmail(response.data.message)
    setUsername(response.data.data)
  }
  useEffect(() => {
    getUser()
  }, [])

  return (
    <header className="header">

      <div className="header-user">
        <div className="user-info">
          <span className="username">{username}</span>
          <span className="email">{email}</span>
        </div>

        <div className="avatar">
          {username.charAt(0).toUpperCase()}
        </div>

      </div>

    </header>
  )
}

export default Header