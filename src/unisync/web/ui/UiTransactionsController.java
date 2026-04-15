package unisync.web.ui;

import model.transaction.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.TransactionService;

import java.util.List;

@Controller
public class UiTransactionsController {

    private final TransactionService transactionService;

    public UiTransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/ui/transactions")
    public String transactions(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "success", required = false) String success,
                               Model model) {
        List<Transaction> tx = transactionService.getAllTransactions();
        model.addAttribute("transactions", tx);
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "transactions";
    }
}

