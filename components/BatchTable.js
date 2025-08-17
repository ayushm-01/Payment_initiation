import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button, Table, Form, Card } from "react-bootstrap";

function BatchTable({batches}) {
  
  const [selected, setSelected] = useState([]);
  React.useEffect(() => {
  setSelected((prevSelected) => prevSelected.filter(id => batches.some(b => b.id === id)));
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
      setSelected(batches.map((b) => b.id));
    }
  };

  const handleAccept = () => {
    alert(`Accepted batches: ${selected.join(", ")}`);
  };

  const handleReject = () => {
    alert(`Rejected batches: ${selected.join(", ")}`);
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
                  checked={selected.length === batches.length}
                  onChange={handleAll}
                />
              </th>
              <th className="fw-semibold">Batch Reference</th>
              <th className="fw-semibold">Batch Name</th>
              <th className="fw-semibold">Created By</th>
              <th className="fw-semibold text-center"># Payments</th>
              <th className="fw-semibold text-end">Total Amount</th>
              <th className="fw-semibold text-center">Currency</th>
              <th className="fw-semibold">Execution Date</th>
            </tr>
          </thead>
          <tbody>
            {batches.map((batch) => (
              <tr
                key={batch.id}
                className={selected.includes(batch.id) ? "table-active" : ""}
                style={{ cursor: "pointer" }}
                onClick={() => handleSelected(batch.id)}
              >
                <td>
                  <Form.Check
                    type="checkbox"
                    checked={selected.includes(batch.id)}
                    onChange={() => handleSelected(batch.id)}
                    onClick={(e) => e.stopPropagation()}
                  />
                </td>
                <td className="fw-medium">{batch.batchId}</td>
                <td>{batch.batchName}</td>
                <td>{batch.createdBy}</td>
                <td className="text-center">{batch.numPayments}</td>
                <td className="text-end text-success fw-semibold">
                  ₹{batch.amount.toLocaleString()}
                </td>
                <td className="text-center">{batch.currency === "INR" ? "₹" : "$"}{batch.amount.toLocaleString()}</td>
                <td>{batch.executionDate}</td>
              </tr>
            ))}
          </tbody>
        </Table>

        <div className="d-flex gap-2 mt-3 justify-content-end">
          <Button
            variant="outline-success"
            onClick={handleAccept}
            disabled={!selected.length}
          >
            ✅ Accept
          </Button>
          <Button
            variant="outline-danger"
            onClick={handleReject}
            disabled={!selected.length}
          >
            ❌ Reject
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
}

export default BatchTable;
