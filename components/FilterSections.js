import React from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { Button, Form, Col, Row, FormControl, InputGroup, Card } from "react-bootstrap";
import { FaSearch, FaTimes } from "react-icons/fa";

function FilterSections({ filters, setFilters }) {
  const { searchTerm, batchId, paymentPeriod, amountRange } = filters;

  const handleChange = (field, value) => {
    setFilters((prev) => ({ ...prev, [field]: value }));
  };

  const handleClearSelection = () => {
    setFilters({
      searchTerm: "",
      batchId: "",
      paymentPeriod: "",
      amountRange: ""
    });
  };

  return (
    <Card className="border-0 rounded-4 mb-4">
      <Card.Body>
        <Form>
          <Row className="g-3 align-items-end">
            <Col md={3}>
              <Form.Label className="fw-semibold">Search</Form.Label>
              <InputGroup>
                <FormControl
                  placeholder="Search batches..."
                  value={searchTerm}
                  onChange={(e) => handleChange("searchTerm", e.target.value)}
                  className="rounded-start-pill"
                />
                <Button
                  variant="primary"
                  className="rounded-end-pill"
                  onClick={() => {}}
                >
                  <FaSearch />
                </Button>
              </InputGroup>
            </Col>

            <Col md={3}>
              <Form.Label className="fw-semibold">Batch ID</Form.Label>
              <Form.Control
                type="text"
                value={batchId}
                onChange={(e) => handleChange("batchId", e.target.value)}
                placeholder="Enter Batch ID"
              />
            </Col>

            <Col md={3}>
              <Form.Label className="fw-semibold">Payment Period</Form.Label>
              <Form.Select
                value={paymentPeriod}
                onChange={(e) => handleChange("paymentPeriod", e.target.value)}
              >
                <option value="">Select Period</option>
                <option value="Jan">January</option>
                <option value="Feb">February</option>
                <option value="Mar">March</option>
              </Form.Select>
            </Col>

            <Col md={3}>
              <Form.Label className="fw-semibold">Min Amount</Form.Label>
              <Form.Control
                type="number"
                value={amountRange}
                onChange={(e) => handleChange("amountRange", e.target.value)}
                placeholder="Enter minimum amount"
              />
            </Col>
          </Row>

          <div className="d-flex justify-content-end mt-4 gap-2">
            <Button
              variant="outline-secondary"
              className="d-flex align-items-center gap-2"
              onClick={handleClearSelection}
            >
              <FaTimes /> Clear
            </Button>
          </div>
        </Form>
      </Card.Body>
    </Card>
  );
}

export default FilterSections;
