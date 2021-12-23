Account subclass: SavingAccount[
    |interest|
    SavingAccount class[
        new[
            |r| r:=super new.
            r init.
            r initInterest.
            ^r
        ]
    ]

    initInterest[
        interest := 0.0
    ]
    " fullConstructor: pNumber customer: pCustomer balance: pBalance[
        number := pNumber.
        customer := pCustomer.
        balance := pBalance
    ] "
    initCustomer: cus [
        customer:=cus
    ]
    initCustomer: cus initNumber: num initBalance: bal [
        customer:=cus.
        number:=num.
        balance:=bal.
    ]
    initNumber: num[
        number:=num
    ]
    initBalance: bal[
        balance:=bal
    ]
    accrue: rate[
        interest := interest + (balance*rate).
        balance := balance + (balance * rate)
    ]
]
|c| c := Customer new: 'Jeffrey From Saving'.
|SA| SA := SavingAccount new initCustomer: c initNumber: '02' initBalance: 200.05.
SA deposit: 100.
SA toString printNl.
SA withdraw: 50.
SA toString printNl.
SA accrue: 0.02.
SA toString printNl