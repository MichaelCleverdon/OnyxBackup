Object subclass: Bank[
    |accounts|
    Bank class[
        new[
            ^super new init
        ]
    ]

    init[
        accounts := Set new
    ]

    add: account[
        accounts add: account
    ]

    accrue: rate[
        accounts do: [ :account | account accrue: rate]
    ]

    toString [
        |string| string := ''.
        accounts do: [ :account | string := string,(account toString), (Character nl asString)].
        ^string
    ]
]

|b| b := Bank new.
|c| c := Customer new: 'Ann'.
b add: (CheckingAccount new initCustomer: c initNumber: '01001' initBalance: 100.00).
b add: (SavingAccount new initCustomer: c initNumber: '01002' initBalance: 200.00).
b accrue: 0.02.
b toString printNl.