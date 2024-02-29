import java.time.LocalDateTime
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.in)
    val expenseTracker = ExpenseTracker()

    while (true) {
        println("""
            |1: Display Balance
            |2: Add Expense
            |3: Add Income
            |4: Cancel Last Transaction
            |5: Display History
            |6: Add Category
            |7: Display Balance by Category
            |8: Exit
            |Please enter a command:
        """.trimMargin())
        when (scanner.nextLine()) {
            "1" -> expenseTracker.displayBalance()
            "2" -> {
                println("Enter amount, description, and category:")
                val amount = scanner.nextLine().toDoubleOrNull()
                val description = scanner.nextLine()
                val category = scanner.nextLine()
                expenseTracker.addTransaction(-1 * (amount ?: 0.0), description, LocalDateTime.now(), category)
            }
            "3" -> {
                println("Enter amount, description, and category:")
                val amount = scanner.nextLine().toDoubleOrNull()
                val description = scanner.nextLine()
                val category = scanner.nextLine()
                expenseTracker.addTransaction(amount ?: 0.0, description, LocalDateTime.now(), category)
            }
            "4" -> expenseTracker.cancelLastTransaction()
            "5" -> expenseTracker.displayHistory()
            "6" -> {
                println("Enter category name:")
                val category = scanner.nextLine()
                expenseTracker.addCategory(category)
            }
            "7" -> {
                println("Enter category name:")
                val category = scanner.nextLine()
                expenseTracker.displayBalanceByCategory(category)
            }
            "8" -> {
                println("Exiting...")
                return
            }
            else -> println("Invalid command. Please try again.")
        }
    }
}

class ExpenseTracker {
    private var balance: Double = 0.0
    private val transactions: MutableList<Transaction> = mutableListOf()
    private val categories: MutableSet<String> = mutableSetOf("Income", "Expense")

    fun addTransaction(amount: Double, description: String, date: LocalDateTime, category: String) {
        if (!categories.contains(category)) {
            println("Category does not exist. Please add the category first.")
            return
        }
        val transaction = Transaction(amount, description, date, category)
        transactions.add(transaction)
        balance += amount
        println("Transaction added successfully.")
    }

    fun displayBalance() {
        println("Current balance: $balance")
    }

    fun cancelLastTransaction() {
        if (transactions.isNotEmpty()) {
            val lastTransaction = transactions.removeLast()
            balance -= lastTransaction.amount
            println("Last transaction canceled successfully.")
        } else {
            println("No transactions to cancel.")
        }
    }

    fun displayHistory() {
        if (transactions.isEmpty()) {
            println("No transactions to display.")
            return
        }
        transactions.forEach { println("${it.date}: ${it.category} - ${it.description} - ${it.amount}") }
    }

    fun addCategory(category: String) {
        if (categories.add(category)) {
            println("Category '$category' added successfully.")
        } else {
            println("Category '$category' already exists.")
        }
    }

    fun displayBalanceByCategory(category: String) {
        if (!categories.contains(category)) {
            println("Category '$category' does not exist.")
            return
        }
        val totalByCategory = transactions.filter { it.category == category }.sumOf { it.amount }
        println("Total for $category: $totalByCategory")
    }
}

data class Transaction(val amount: Double, val description: String, val date: LocalDateTime, val category: String)
