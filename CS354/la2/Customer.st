Object subclass: Customer [
    |name|

    Customer class [
        new: pName[
            ^super new init: pName
        ]
    ]

    init: pName[
        name:=pName
    ]

    toString [
       ^name asString
    ]
]

|customer| customer:= Customer new: 'Jeffrey the Customer'.