import React, { useState, useMemo } from "react";
import BatchTable from "./BatchTable";
import HeaderRow from "./HeaderRow";
import FilterSections from "./FilterSections";
import * as XLSX from "xlsx";
import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";

export default function FilterImplementation() {
  const [filters, setFilters] = useState({
    searchTerm: "",
    batchId: "",
    currency: "",
    amountRange: "",
  });

  const [batches, setBatches] = useState([]);

  // ✅ Apply filters
  const filteredBatches = useMemo(() => {
    return batches.filter((batch) => {
      const { searchTerm, batchId, currency, amountRange } = filters;

      if (
        searchTerm &&
        !(
          batch.batchName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
          batch.createdBy?.userId?.toLowerCase().includes(searchTerm.toLowerCase())
        )
      ) return false;

      if (batchId && !batch.batchId.toString().includes(batchId)) return false;
      if (currency && batch.currency !== currency) return false;

      if (amountRange) {
        const min = parseFloat(amountRange);
        if (!isNaN(min) && batch.totAmt < min) return false;
      }

      return true;
    });
  }, [filters, batches]);

  // ✅ Export functions
  const exportAsPdf = () => {
    const doc = new jsPDF();
    doc.text("Batches Report", 14, 18);
    autoTable(doc, {
      head: [["Batch ID", "Batch Name", "Created By", "Currency", "Amount"]],
      body: filteredBatches.map((row) => [
        row.batchId,
        row.batchName,
        row.createdBy?.userId,
        row.currency,
        row.totAmt,
      ]),
    });
    doc.save("batches.pdf");
  };

  const exportAsExcel = () => {
    const worksheet = XLSX.utils.json_to_sheet(filteredBatches);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Batches");
    XLSX.writeFile(workbook, "batches.xlsx");
  };

  return (
    <div>
      <HeaderRow exportAsPdf={exportAsPdf} exportAsExcel={exportAsExcel} />
      <FilterSections filters={filters} setFilters={setFilters} />
      <BatchTable batches={filteredBatches} setBatches={setBatches} />
    </div>
  );
}
