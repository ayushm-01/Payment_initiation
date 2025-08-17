import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Navbar, Container, Nav } from "react-bootstrap";
import { BellFill } from "react-bootstrap-icons";

function Dashboard({ children, role }) {
  const menuItems = {
    batching: ["Dashboard", "Accept Payroll"],
    approver1: ["Dashboard", "Pending Approvals"],
    approver2: ["Dashboard", "Pending Approvals"],
    approver3: ["Dashboard", "Pending Approvals"],
    release: ["Dashboard", "Release to Bank", "History"]
  };

  const items = menuItems[role] || ["Dashboard"];

  const activeMap = {
    batching: "Accept Payroll",
    approver1: "Pending Approvals",
    approver2: "Pending Approvals",
    approver3: "Pending Approvals",
    release: "Release to Bank"
  };
  const activeItem = activeMap[role] || "Dashboard";

  return (
    <div className="d-flex" style={{ minHeight: "100vh" }}>
      {/* Sidebar */}
      <div
        style={{
          width: "220px",
          background: "linear-gradient(180deg, #0d6efd 0%, #084298 100%)",
          color: "white",
          paddingTop: "2rem",
          display: "flex",
          flexDirection: "column",
          boxShadow: "2px 0px 6px rgba(0,0,0,0.1)"
        }}
      >
        <Nav className="flex-column px-2">
          {items.map((item, idx) => (
            <Nav.Link
              key={idx}
              href="#"
              className={`rounded px-3 py-2 mb-1 fw-semibold ${
                item === activeItem
                  ? "bg-white text-primary shadow-sm"
                  : "text-white"
              }`}
              style={{ transition: "0.2s" }}
              onMouseEnter={(e) => {
                if (item !== activeItem) e.target.style.background = "#0b5ed7";
              }}
              onMouseLeave={(e) => {
                if (item !== activeItem) e.target.style.background = "transparent";
              }}
            >
              {item}
            </Nav.Link>
          ))}
        </Nav>
      </div>

      {/* Right side */}
      <div className="flex-grow-1">
        <Navbar bg="white" className="shadow-sm py-2">
          <Container fluid>
            <div className="d-flex align-items-center">
              <img
                src="https://upload.wikimedia.org/wikipedia/commons/thumb/2/21/Standard_Chartered.svg/2560px-Standard_Chartered.svg.png"
                alt="Logo"
                style={{ height: "28px", marginRight: "12px" }}
              />
            </div>
            <div className="d-flex align-items-center">
              <BellFill
                size={20}
                className="me-3 text-secondary"
                style={{ cursor: "pointer" }}
              />
              <span className="fw-bold text-primary">Ayush Mangy</span>
            </div>
          </Container>
        </Navbar>

        <div className="p-4 bg-light" style={{ minHeight: "calc(100vh - 56px)" }}>
          {children}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;
