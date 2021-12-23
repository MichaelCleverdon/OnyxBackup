Account subclass: CheckingAccount[
    CheckingAccount class[
        new[
            ^super new init.
        ]
    ]
    " init: pNumber balance: pBalance customer: pCustomer[
        number:=pNumber.
        balance:=pBalance.
        customer:=pCustomer
    ] "

    " fullConstructor: pNumber customer: pCustomer balance: pBalance[
        number := pNumber.
        customer := pCustomer.
        balance := pBalance
    ] "
    initCustomer: cus initNumber: num initBalance: bal [
        customer:=cus.
        number:=num.
        balance:=bal
    ]
    initNumber: num[
        number := num
    ]
    initCustomer: cus[
        customer := cus
    ]
    initBalance: bal[
        balance := bal
    ]
    accrue: rate[
    ]
]
|c| c := Customer new: 'Jeffrey from Checking'.
|CA| CA := CheckingAccount new initCustomer: c initNumber: '01' initBalance: 100.00.
CA toString printNl.
CA deposit: 100.
CA toString printNl.
CA withdraw: 50.
CA toString printNl.
"Intentionally doing nothing"
CA accrue: 0.02.
CA toString printNl