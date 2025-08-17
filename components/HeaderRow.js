import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Dropdown, ButtonGroup } from "react-bootstrap";
import { FaFilePdf, FaFileExcel, FaDownload } from "react-icons/fa";

export default function HeaderRow() {
  const exportAsPdf = () => {
    alert("Exporting as PDF");
  };

  const exportAsExcel = () => {
    alert("Exporting as Excel");
  };

  return (
    <div className="container-fluid py-3 rounded-3 mb-4">
      <div className="d-flex justify-content-between align-items-center">

        <h4 className="m-0 fw-bold ">Approve Batches</h4>


        <Dropdown as={ButtonGroup}>
          <Dropdown.Toggle
            variant="primary"
            className="px-4 d-flex align-items-center gap-2"
          >
            <FaDownload /> Export
          </Dropdown.Toggle>

          <Dropdown.Menu align="end">
            <Dropdown.Item onClick={exportAsPdf} className="d-flex gap-2 align-items-center">
              <FaFilePdf color="red" /> Export as PDF
            </Dropdown.Item>
            <Dropdown.Item onClick={exportAsExcel} className="d-flex gap-2 align-items-center">
              <FaFileExcel color="green" /> Export as Excel
            </Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </div>
    </div>
  );
}
