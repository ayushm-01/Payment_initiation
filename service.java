import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button, Table, Form, Card } from "react-bootstrap";
import axios from "axios";

function BatchTable() {
  const [batches, setBatches] = useState([]);
  const [selected, setSelected] = useState([]);

  // Fetch batches from backend
  useEffect(() => {
    axios
      .get("http://localhost:8080/api/approver/batches")
      .then((res) => setBatches(res.data))
      .catch((err) => console.error("Error fetching batches:", err));
  }, []);

  // Keep selected valid if list updates
  useEffect(() => {
    setSelected((prev) =>
      prev.filter((id) => batches.some((b) => b.batchId === id))
    );
  }, [batches]);

  const handleSelected = (id) => {
    setSelected((prev) =>
      prev.includes(id) ? prev.filter((sid) => sid !== id) : [...prev, id]
    );
  };

  const handleAll = () => {
    if (selected.length === batches.length) {
      setSelected([]);
    } else {
      setSelected(batches.map((b) => b.batchId));
    }
  };

  // Call backend for Accept/Reject
  const handleDecision = async (approved) => {
    try {
      await axios.post("http://localhost:8080/api/approver/decision", {
        approverId: 1, // TODO: replace with logged-in approverId
        batchIds: selected,
        approved: approved,
      });

      alert(
        approved
          ? `✅ Accepted batches: ${selected.join(", ")}`
          : `❌ Rejected batches: ${selected.join(", ")}`
      );

      // Refresh list after decision
      const res = await axios.get("http://localhost:8080/api/approver/batches");
      setBatches(res.data);
      setSelected([]);
    } catch (err) {
      console.error("Error submitting decision:", err);
      alert("Failed to update decision");
    }
  };

  return (
    <Card className="border-0 rounded-4">
      <Card.Body>
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h4 className="fw-bold m-0">Batch Approval</h4>
        </div>

        <Table hover responsive borderless className="align-middle">
          <thead className="bg-light">
            <tr>
              <th style={{ width: "50px" }}>
                <Form.Check
                  type="checkbox"
                  checked={selected.length === batches.length && batches.length > 0}
                  onChange={handleAll}
                />
              </th>
              <th className="fw-semibold">Batch Reference</th>
              <th className="fw-semibold">Batch Name</th>
              <th className="fw-semibold">Created By</th>
              <th className="fw-semibold text-center"># Payments</th>
              <th className="fw-semibold text-end">Total Amount</th>
              <th className="fw-semibold text-center">Currency</th>
              <th className="fw-semibold">Status</th>
            </tr>
          </thead>
          <tbody>
            {batches.map((batch) => (
              <tr
                key={batch.batchId}
                className={selected.includes(batch.batchId) ? "table-active" : ""}
                style={{ cursor: "pointer" }}
                onClick={() => handleSelected(batch.batchId)}
              >
                <td>
                  <Form.Check
                    type="checkbox"
                    checked={selected.includes(batch.batchId)}
                    onChange={() => handleSelected(batch.batchId)}
                    onClick={(e) => e.stopPropagation()}
                  />
                </td>
                <td className="fw-medium">{batch.yourRef}</td>
                <td>{batch.batchName}</td>
                <td>{batch.createdBy?.userId}</td>
                <td className="text-center">{batch.numOfPayments}</td>
                <td className="text-end text-success fw-semibold">
                  ₹{batch.totAmt?.toLocaleString()}
                </td>
                <td className="text-center">{batch.currency}</td>
                <td>{batch.status}</td>
              </tr>
            ))}
          </tbody>
        </Table>

        <div className="d-flex justify-content-between align-items-center mt-3">
          <div className="d-flex gap-2">
            <Button
              variant="outline-success"
              onClick={() => handleDecision(true)}
              disabled={!selected.length}
            >
              ✅ Accept
            </Button>
            <Button
              variant="outline-danger"
              onClick={() => handleDecision(false)}
              disabled={!selected.length}
            >
              ❌ Reject
            </Button>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default BatchTable;
