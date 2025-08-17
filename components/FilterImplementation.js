import React, { useMemo } from "react";
import FilterSections from "./FilterSections";
import BatchTable from "./BatchTable";
import { Filter } from "react-bootstrap-icons";

export default function FilterImplementation() {
     const [filters, setFilters] = React.useState({
        searchTerm: "",
        batchId: "",
        paymentPeriod: "",
        amountRange: ""
    });

     const batches = [
    {
      id: 1,
      batchId: "bqjw2",
      batchName: "Vendor Payment Jan",
      createdBy: "Ayush",
      numPayments: 5,
      amount: 34334.3,
      currency: "INR",
      period: "Jan 2025",
      executionDate: "2025-01-15",
    },
    {
      id: 2,
      batchId: "bqjw3",
      batchName: "Salary Batch Jan",
      createdBy: "Daksh",
      numPayments: 12,
      amount: 120000,
      currency: "INR",
      period: "Jan 2025",
      executionDate: "2025-01-20",
    },
    {
      id: 3,
      batchId: "bqjw4",
      batchName: "Reimbursements Q1",
      createdBy: "Finance Team",
      numPayments: 8,
      amount: 56000,
      currency: "USD",
      period: "Mar 2025",
      executionDate: "2025-03-05",
    },
    {
      id: 4,
      batchId: "bqjw6",
      batchName: "Vendor Payment Feb",
      createdBy: "Ayush",
      numPayments: 4,
      amount: 25000,
      currency: "INR",
      period: "Feb 2025",
      executionDate: "2025-02-10",
    },
  ];

  const filteredBatches = useMemo(() => {
    return batches.filter((batch) =>{
         const { searchTerm, batchId, paymentPeriod, amountRange } = filters;

         if (
        searchTerm &&
        !(
          batch.batchName.toLowerCase().includes(searchTerm.toLowerCase()) ||
          batch.createdBy.toLowerCase().includes(searchTerm.toLowerCase())
        )
      )
        return false;

      // batchId    filter
      if (batchId && !batch.batchId.toLowerCase().includes(batchId.toLowerCase()))
        return false;

      // paymentPeriod filter (check substring)
      if (paymentPeriod && !batch.period.toLowerCase().includes(paymentPeriod.toLowerCase()))
        return false;

     
      if (amountRange) {
  const min = parseFloat(amountRange);
  if (!isNaN(min) && batch.amount < min) return false;
}

      return true;
    });
}, [filters, batches]);

return(
    <div>
    <FilterSections filters = {filters} setFilters = {setFilters}/>
    <BatchTable batches={filteredBatches} />
    </div>
);
}