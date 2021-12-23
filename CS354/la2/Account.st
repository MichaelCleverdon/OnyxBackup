Object subclass: Account [
    |number|
    |customer|
    |balance|
    Account class[
        new[
            ^super new init
        ]

    ]

    init[
        number := 0.
        customer := Customer new: 'Jeffrey from Accounting'.
        balance := 0
    ]    

    accrue[
        ^self subclassResponsibility
    ]   

    deposit: amount[
        balance := balance + amount.
    ]

    withdraw: amount [
        balance := balance - amount.
    ]

    toString[
        ^(number asString,':', customer toString,':', balance asString)
    ]

]
"Should not create an account instance because it's not fully implemented (abstract)"
" |a| a := Account new. 
a toString printNl. "
"
a deposit: 100.
a toString.
a withdraw: 50.
a toString "