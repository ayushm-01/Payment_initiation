import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Card, Table, Form, Button } from "react-bootstrap";

export default function SubmitApproval() {
  const [comments, setComments] = useState("");
  const [password, setPassword] = useState("");

  const handleApprove = () => {
    alert("✅ Batch Approved!");
  };

  const handleReject = () => {
    alert("❌ Batch Rejected!");
  };

  return (
    <div className="container mt-4">
      <Card className="mb-4 shadow-sm border-0">
        <Card.Header className="fw-bold bg-primary text-white">
          Batch Information
        </Card.Header>
        <Card.Body>
          <Table striped bordered hover responsive>
            <tbody>
              <tr>
                <th>Batch Reference</th>
                <td>BR-12345</td>
              </tr>
              <tr>
                <th>Batch Name</th>
                <td>August Payroll</td>
              </tr>
              <tr>
                <th>Created By</th>
                <td>Ayush</td>
              </tr>
              <tr>
                <th># Payments</th>
                <td>25</td>
              </tr>
              <tr>
                <th>Total Amount</th>
                <td>₹ 5,00,000</td>
              </tr>
              <tr>
                <th>Currency</th>
                <td>INR</td>
              </tr>
              <tr>
                <th>Execution Date</th>
                <td>16 Aug 2025</td>
              </tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Batch Summary */}
      <Card className="mb-4 shadow-sm border-0">
        <Card.Header className="fw-bold bg-primary text-white">
          Batch Summary (By Country / Payment Type)
        </Card.Header>
        <Card.Body>
          <Table striped bordered hover responsive>
            <thead>
              <tr>
                <th>Country</th>
                <th>Payment Type</th>
                <th># Payments</th>
                <th>Amount</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>China</td>
                <td>Salary</td>
                <td>10</td>
                <td>¥ 2,50,000</td>
              </tr>
              <tr>
                <td>India</td>
                <td>Salary</td>
                <td>15</td>
                <td>₹ 2,50,000</td>
              </tr>
            </tbody>
          </Table>
        </Card.Body>
      </Card>

      {/* Approval Details */}
      <Card className="mb-4 shadow-sm border-0">
        <Card.Header className="fw-bold bg-primary text-white">
          Approval Details
        </Card.Header>
        <Card.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Comments</Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                value={comments}
                onChange={(e) => setComments(e.target.value)}
                placeholder="Enter your comments (optional)"
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Password Validation</Form.Label>
              <Form.Control
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Enter your password"
              />
            </Form.Group>
          </Form>
        </Card.Body>
      </Card>

      {/* Action Buttons */}
      <div className="d-flex justify-content-end gap-3">
        <Button variant="success" className="px-4" onClick={handleApprove}>
          Approve Batch
        </Button>
        <Button variant="danger" className="px-4" onClick={handleReject}>
          Reject Batch
        </Button>
      </div>
    </div>
  );
}
