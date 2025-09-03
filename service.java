@RestController
@RequestMapping("/approval")
public class ApprovalController {

    @Autowired private ApprovalService approvalService;
    @Autowired private UserRepository userRepo;

    // ---------------- Approver Endpoints ----------------

    // Get all batches pending for approver
    @GetMapping("/approver/pending")
    public List<Batch> getPendingBatchesForApprover() {
        return approvalService.getPendingBatchesForApprover();
    }

    // Approver approves or rejects a batch (no password, no comment)
    @PostMapping("/approver/{batchId}")
    public String approverDecision(@PathVariable Long batchId,
                                   @RequestParam String username,
                                   @RequestParam boolean approved) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only approver role allowed
        if (user.getRole() != RoleType.APPROVER) {
            throw new RuntimeException("User is not an approver");
        }

        approvalService.approverDecision(batchId, user, approved);
        return "Approver decision recorded";
    }

    // ---------------- Manager Endpoints ----------------

    // Manager approves or rejects a batch (requires password + optional comment)
    @PostMapping("/manager/{batchId}")
    public String managerDecision(@PathVariable Long batchId,
                                  @RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam boolean approved,
                                  @RequestParam(required = false) String comment) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Only manager role allowed
        if (user.getRole() != RoleType.MANAGER) {
            throw new RuntimeException("User is not a manager");
        }

        approvalService.managerDecision(batchId, user, password, approved, comment);
        return "Manager decision recorded";
    }
}
