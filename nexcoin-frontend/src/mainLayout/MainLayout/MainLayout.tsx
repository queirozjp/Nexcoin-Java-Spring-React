import Header from "./Header/Header"
import Sidebar from "./Sidebar/Sidebar"
import { Outlet } from "react-router-dom"
import './mainlayoutstyle.css'

function MainLayout() {
  return (
    <div className="layout">

      <Sidebar />

      <div className="main">

        <Header/>

        <div className="content">
          <Outlet />
        </div>

      </div>

    </div>
  )
}

export default MainLayout