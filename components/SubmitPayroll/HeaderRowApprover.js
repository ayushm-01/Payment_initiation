import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Dropdown, ButtonGroup } from "react-bootstrap";
import { FaFilePdf, FaFileExcel, FaDownload } from "react-icons/fa";

export default function HeaderRow() {
  return (
    <div className="d-flex justify-content-between align-items-center">
      <h4 className="m-0 fw-bold">Submit Approval</h4>
    </div>
  );
}
