fun main() {
    println("Welcome to your banking system.")
    println("What type of account would you like to create?")
    println("1. Debit account")
    println("2. Credit account")
    println("3. Checking account")
    var accountType = ""
    var userChoice = 0
    while (accountType == "") {
       println("Choose an option (1, 2 or 3)")
       userChoice = (1..3).random()
       println("The selected option is $userChoice.")
       
       accountType = when (userChoice) {
           1 -> "debit"
           2 -> "credit"
           3 -> "checking"
           else -> ""
       }
    }
    println("You have created a $accountType account.")
 
    var accountBalance = (0..1000).random()
    println("The current balance is $accountBalance dollars.")
    val money = (0..1000).random()
    println("The amount you transferred is $money dollars.")

    var isSystemOpen = true
    var option = 0
    
    while (isSystemOpen) {
        println("What would you like to do?")
        println("1. Check bank account balance")
        println("2. Withdraw money")
        println("3. Deposit money")
        println("4. Close the system")
        println("Which option do you choose? (1, 2, 3, or 4)")

        option = readLine()?.toIntOrNull() ?: continue

        when (option) {
            1 -> println("The current balance is $accountBalance dollars.")
            2 -> transfer("withdraw", accountType, money, ::accountBalance, ::withdraw, ::debitWithdraw)
            3 -> transfer("deposit", accountType, money, ::accountBalance, ::deposit, ::creditDeposit)
            4 -> {
                isSystemOpen = false
                println("The system is closed")
            }
            else -> println("Invalid option. Please choose again.")
        }
    }
}

fun transfer(
    mode: String,
    accountType: String,
    money: Int,
    accountBalance: () -> Int,
    withdrawFunction: (Int) -> Int,
    specialWithdrawFunction: (Int) -> Int
) {
    val transferAmount: Int

    when (mode) {
        "withdraw" -> {
            transferAmount = if (accountType == "debit") {
                specialWithdrawFunction(money)
            } else {
                withdrawFunction(money)
            }
            println("The amount you withdrew is $transferAmount dollars.")
        }
        "deposit" -> {
            transferAmount = if (accountType == "credit") {
                specialWithdrawFunction(money)
            } else {
                withdrawFunction(money)
            }
            println("The amount you deposited is $transferAmount dollars.")
        }
        else -> return
    }
}

fun withdraw(amount: Int): Int {
    var balance = accountBalance()
    balance -= amount
    println("You successfully withdrew $amount dollars. The current balance is $balance dollars.")
    return amount
}

fun debitWithdraw(amount: Int): Int {
    var balance = accountBalance()
    if (balance == 0) {
        println("Can't withdraw, no money on this account!")
        return balance
    } else if (amount > balance) {
        println("Not enough money on this account! The current balance is $balance dollars.")
        return 0
    } else {
        return withdraw(amount)
    }
}

fun deposit(amount: Int): Int {
    var balance = accountBalance()
    balance += amount
    println("You successfully deposited $amount dollars. The current balance is $balance dollars.")
    return amount
}

fun creditDeposit(amount: Int): Int {
    var balance = accountBalance()
    if (balance == 0) {
        println("This account is completely paid off! Do not deposit money!")
        return balance
    } else if (balance + amount > 0) {
        println("Deposit failed, you tried to pay off an amount greater than the credit balance. The checking balance is $balance dollars.")
        return 0
    } else if (amount == -balance) {
        balance = 0
        println("You have paid off this account!")
        return amount
    } else {
        return deposit(amount)
    }
}
